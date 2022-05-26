package mspets.my.zoomod.client.entities.models;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mspets.my.zoomod.MyZooMod;
import mspets.my.zoomod.common.entities.CrocodileEntity;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CrocodileModel<Type extends CrocodileEntity> extends AgeableListModel<Type>
{
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MyZooMod.MODID, "crocodile"), "main");
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart frontRightLeg;
    private final ModelPart frontLeftLeg;
    private final ModelPart backRightLeg;
    private final ModelPart backLeftLeg;
    private final ModelPart tailModel;
    private final ModelPart tail2Model;
    private final ModelPart tail3Model;

    public CrocodileModel(ModelPart root)
    {
        this.body = root.getChild("Body");
        this.head = body.getChild("Head");
        ModelPart frontLegs = body.getChild("FrontLegs");
        this.frontRightLeg = frontLegs.getChild("FrontLeft");
        this.frontLeftLeg = frontLegs.getChild("FrontRight");
        ModelPart backLegs = body.getChild("BackLegs");
        this.backRightLeg = backLegs.getChild("BackLeft");
        this.backLeftLeg = backLegs.getChild("BackRight");

        this.tailModel = body.getChild("Tail");
        this.tail2Model = this.tailModel.getChild("Tail2");
        this.tail3Model = this.tail2Model.getChild("Tail3");
    }

    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -5.0F, -25.0F, 16.0F, 10.0F, 50.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));

        PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 60).addBox(-6.0F, -4.0F, -20.0F, 14.0F, 8.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, -25.0F));

        PartDefinition FrontLegs = Body.addOrReplaceChild("FrontLegs", CubeListBuilder.create(), PartPose.offset(0.0F, 9.0F, 0.0F));
        PartDefinition FrontLeft = FrontLegs.addOrReplaceChild("FrontLeft", CubeListBuilder.create().texOffs(24, 26).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, -6.0F, -22.0F));
        PartDefinition FrontRight = FrontLegs.addOrReplaceChild("FrontRight", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, -6.0F, -22.0F));

        PartDefinition BackLegs = Body.addOrReplaceChild("BackLegs", CubeListBuilder.create(), PartPose.offset(0.0F, 9.0F, 0.0F));
        PartDefinition BackLeft = BackLegs.addOrReplaceChild("BackLeft", CubeListBuilder.create().texOffs(0, 18).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, -6.0F, 22.0F));
        PartDefinition BackRight = BackLegs.addOrReplaceChild("BackRight", CubeListBuilder.create().texOffs(14, 18).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, -6.0F, 22.0F));

        PartDefinition Tail = Body.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(68, 60).addBox(-6.0F, -3.0F, 0.0F, 12.0F, 6.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 25.0F));
        PartDefinition Tail2 = Tail.addOrReplaceChild("Tail2", CubeListBuilder.create().texOffs(82, 0).addBox(-5.0F, -2.0F, 0.0F, 10.0F, 4.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 16.0F));
        PartDefinition Tail3 = Tail2.addOrReplaceChild("Tail3", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 16.0F));

        return LayerDefinition.create(meshdefinition, 160, 160);
    }

    @Override
    public void prepareMobModel(Type pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick)
    {

    }

    @Override
    public void setupAnim(Type entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {

        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.frontRightLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.frontLeftLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.backRightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.backLeftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;

        this.tailModel.yRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * (limbSwingAmount * .2F);
        this.tail2Model.yRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * (limbSwingAmount * .2F);
        this.tail3Model.yRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * (limbSwingAmount * .2F);


        float age = ageInTicks - (float) entity.tickCount;
        float scale = entity.getStandingAnimationScale(age);
        scale *= scale;
        float newScale = 1.0F - scale;

        this.body.xRot = ((float)Math.PI / 2F) - scale * (float)Math.PI * 0.35F;
        this.body.y = 9.0F * newScale + 11.0F * scale;
        this.frontRightLeg.y = 14.0F * newScale - 6.0F * scale;
        this.frontRightLeg.z = -8.0F * newScale - 4.0F * scale;
        this.frontRightLeg.xRot -= newScale * (float)Math.PI * 0.45F;
        this.frontLeftLeg.y = this.frontRightLeg.y;
        this.frontLeftLeg.z = this.frontRightLeg.z;
        this.frontLeftLeg.xRot -= scale * (float)Math.PI * 0.45F;
        if (this.young) {
            this.head.y = 10.0F * newScale - 9.0F * scale;
            this.head.z = -16.0F * newScale - 7.0F * scale;
        } else {
            this.head.y = 10.0F * newScale - 14.0F * scale;
            this.head.z = -16.0F * newScale - 3.0F * scale;
        }

        this.head.xRot += scale * (float)Math.PI * 0.15F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.backRightLeg, this.backLeftLeg, this.frontRightLeg, this.frontLeftLeg);
    }
}