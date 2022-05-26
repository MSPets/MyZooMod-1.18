package mspets.my.zoomod.common.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Predicate;

// https://forums.minecraftforge.net/topic/92625-1163-cant-register-entity-with-deferred-register/
public class CrocodileEntity extends Animal implements NeutralMob
{
    // ANIMATION
    private static final EntityDataAccessor<Boolean> DATA_STANDING_ID = SynchedEntityData.defineId(CrocodileEntity.class, EntityDataSerializers.BOOLEAN);
    private static final float STAND_ANIMATION_TICKS = 6.0f;
    private float clientSideStandAnimationO;
    private float clientSideStandAnimation;
    // ANGER
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private int remainingPersistentAngerTime;
    @Nullable
    private UUID persistentAngerTarget;


    public CrocodileEntity(EntityType<? extends Animal> type, Level worldIn)
    {
        super(type, worldIn);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob)
    {
        return null;
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        int priority = 0;
        // TODO Make sure this works
        this.goalSelector.addGoal(priority++, new FloatGoal(this));
        this.goalSelector.addGoal(priority++, new CrocodileEntity.CrocodileMeleeAttackGoal());
        this.goalSelector.addGoal(priority++, new CrocodileEntity.CrocodilePanicGoal());
        this.goalSelector.addGoal(priority++, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(priority++, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(priority++, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(priority++, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(priority++, new CrocodileEntity.CrocodileHurtByTargetGoal());
        this.targetSelector.addGoal(priority++, new CrocodileEntity.CrocodileAttackPlayerGoal());
        this.targetSelector.addGoal(priority++, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(priority++, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    // DATA
    @Override
    public void readAdditionalSaveData(CompoundTag compound)
    {
        super.readAdditionalSaveData(compound);
        this.readPersistentAngerSaveData(this.level, compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound)
    {
        super.addAdditionalSaveData(compound);
        this.addAdditionalSaveData(compound);
    }

    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(DATA_STANDING_ID, false);
    }

    public static AttributeSupplier.Builder prepareAttributes()
    {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FOLLOW_RANGE, 40)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    // ANGER / ATTACKING
    public int getRemainingPersistentAngerTime()
    {
        return this.remainingPersistentAngerTime;
    }

    public void setRemainingPersistentAngerTime(int time)
    {
        this.remainingPersistentAngerTime = time;
    }

    @Nullable
    public UUID getPersistentAngerTarget()
    {
        return this.persistentAngerTarget;
    }

    public void setPersistentAngerTarget(@Nullable UUID target)
    {
        this.persistentAngerTarget = target;
    }

    public void startPersistentAngerTimer()
    {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    public boolean doHurtTarget(Entity entity)
    {
        boolean flag = entity.hurt(DamageSource.mobAttack(this), (float) ((int) this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (flag)
        {
            this.doEnchantDamageEffects(this, entity);
        }
        return flag;
    }

    // TODO get sounds
    // TODO natural spawns

    // Animations
    public EntityDimensions getDimensions(Pose pose)
    {
        if (this.clientSideStandAnimation > 0.0F)
        {
            float f = this.clientSideStandAnimation / 6.0F;
            float f1 = 1.0F + f;
            return super.getDimensions(pose).scale(1.0F, f1);
        }
        else
        {
            return super.getDimensions(pose);
        }
    }

    public boolean isStanding()
    {
        return this.entityData.get(DATA_STANDING_ID);
    }

    public void setStanding(boolean standing)
    {
        this.entityData.set(DATA_STANDING_ID, standing);
    }

    public float getStandingAnimationScale(float f)
    {
        return Mth.lerp(f, this.clientSideStandAnimationO, this.clientSideStandAnimation) / 6.0F;
    }

    public void tick()
    {
        super.tick();
        if (this.level.isClientSide)
        {
            if (this.clientSideStandAnimation != this.clientSideStandAnimationO)
            {
                this.refreshDimensions();
            }
            this.clientSideStandAnimationO = this.clientSideStandAnimation;
            if (this.isStanding())
            {
                this.clientSideStandAnimation = Mth.clamp(this.clientSideStandAnimation + 1.0F, 0.0F, 6.0F);
            }
            else
            {
                this.clientSideStandAnimation = Mth.clamp(this.clientSideStandAnimation - 1.0F, 0.0F, 6.0F);
            }
        }

        if (!this.level.isClientSide)
        {
            this.updatePersistentAnger((ServerLevel) this.level, true);
        }
    }

    class CrocodileAttackPlayerGoal extends NearestAttackableTargetGoal<Player>
    {
        public CrocodileAttackPlayerGoal()
        {
            super(CrocodileEntity.this, Player.class, 20, true, true, (Predicate<LivingEntity>) null);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse()
        {
            if (CrocodileEntity.this.isBaby())
            {
                return false;
            }
            else
            {
                if (super.canUse())
                {
                    for (CrocodileEntity crocodileEntity : CrocodileEntity.this.level.getEntitiesOfClass(CrocodileEntity.class, CrocodileEntity.this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D)))
                    {
                        if (crocodileEntity.isBaby())
                        {
                            return true;
                        }
                    }
                }
                return false;
            }
        }

        protected double getFollowDistance()
        {
            return super.getFollowDistance() * 0.5D;
        }
    }

    class CrocodileHurtByTargetGoal extends HurtByTargetGoal
    {
        public CrocodileHurtByTargetGoal()
        {
            super(CrocodileEntity.this);
        }
        /**
         * Execute a one shot task or start executing a continuous task
         */
        /*
        @Override
        public void start()
        {
            super.start();
            if (CrocodileEntity.this.isBaby())
            {
                this.alertOthers();
                this.stop();
            }
        }
        protected void alertOther(Mob mob, LivingEntity target)
        {
            if(mob instanceof CrocodileEntity && !mob.isBaby())
            {
                super.alertOther(mob, target);
            }
        }
         */
    }

    class CrocodileMeleeAttackGoal extends MeleeAttackGoal
    {
        public CrocodileMeleeAttackGoal()
        {
            super(CrocodileEntity.this, 1.25D, true);
        }

        protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemy)
        {
            double d = this.getAttackReachSqr(enemy);
            if (distToEnemy <= d && this.isTimeToAttack())
            {
                this.resetAttackCooldown();
                this.mob.doHurtTarget(enemy);
                CrocodileEntity.this.setStanding(false);
            }
            else if (distToEnemy <= d * 2.0D)
            {
                if (this.isTimeToAttack())
                {
                    CrocodileEntity.this.setStanding(false);
                    this.resetAttackCooldown();
                }

                if (this.getTicksUntilNextAttack() <= 10)
                {
                    CrocodileEntity.this.setStanding(true);
                }
            }
            else
            {
                this.resetAttackCooldown();
                CrocodileEntity.this.setStanding(false);
            }
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop()
        {
            CrocodileEntity.this.setStanding(false);
            super.stop();
        }

        protected double getAttackReachSqr(LivingEntity target)
        {
            return (double) (4.0F + target.getBbWidth());
        }
    }

    class CrocodilePanicGoal extends PanicGoal
    {
        public CrocodilePanicGoal()
        {
            super(CrocodileEntity.this, 2.0D);
        }

        protected boolean shouldPanic()
        {
            return this.mob.getLastHurtByMob() != null && this.mob.isBaby() || this.mob.isOnFire();
        }
    }
}
