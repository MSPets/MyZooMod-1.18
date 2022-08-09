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

import static mspets.my.zoomod.MyZooMod.LOGGER;



// https://forums.minecraftforge.net/topic/92625-1163-cant-register-entity-with-deferred-register/
public class CrocodileEntity extends Animal implements NeutralMob
{
    // ANGER
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private static final EntityDataAccessor<Boolean> IS_LYING = SynchedEntityData.defineId(CrocodileEntity.class, EntityDataSerializers.BOOLEAN);

    private int remainingPersistentAngerTime;
    @Nullable
    private UUID persistentAngerTarget;

    private float lieDownAmount;
    private float lieDownAmountO;

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
        //this.goalSelector.addGoal(priority++, new CrocodileEntity.CrocodileLaydownGoal());
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
        this.addPersistentAngerSaveData(compound);
    }

    public static AttributeSupplier.Builder prepareAttributes()
    {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FOLLOW_RANGE, 40)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(IS_LYING, false);
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
    // TODO setup laying down animation

    // Lying down
    public void setLying(boolean bool)
    {
        this.entityData.set(IS_LYING, bool);
    }

    public boolean isLying()
    {
        return this.entityData.get(IS_LYING);
    }

    public float getLieDownAmount(float f)
    {
        return Mth.lerp(f, this.lieDownAmountO, this.lieDownAmount);
    }

    public void tick()
    {
        super.tick();

        if (!this.level.isClientSide)
        {
            this.updatePersistentAnger((ServerLevel) this.level, true);
        }
        // Liedown
        this.lieDownAmountO = this.lieDownAmount;
        if (this.isLying())
        {
            this.lieDownAmount = Math.min(1.0F, this.lieDownAmount + 0.15F);
        }
        else
        {
            this.lieDownAmount = Math.max(0.0F, this.lieDownAmount - 0.22F);
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose)
    {
        return super.getDimensions(pPose).scale(1.4F, 1);
    }

    // Goal Classes
    class CrocodileLaydownGoal extends Goal
    {
        @Override
        public void start()
        {
            CrocodileEntity.this.setLying(false);
        }

        @Override
        public boolean canUse()
        {
            return !CrocodileEntity.this.isLying();
        }

        @Override
        public void stop()
        {
            CrocodileEntity.this.setLying(false);
        }

        @Override
        public void tick()
        {
            if (!CrocodileEntity.this.isLying())
            {
                CrocodileEntity.this.setLying(true);
            }
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
            }
            /*
            else if (distToEnemy <= d * 2.0D)
            {
                LOGGER.error("TO FAR AWAY");
                if (this.isTimeToAttack())
                {
=                    this.resetAttackCooldown();
                }
            }
            else
            {
                this.resetAttackCooldown();
            }
             */
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop()
        {
            super.stop();
        }

        protected double getAttackReachSqr(LivingEntity target)
        {
            return (double) (8.0F + target.getBbWidth());
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
