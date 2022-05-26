package mspets.my.zoomod.client.entities.renderers;

import mspets.my.zoomod.MyZooMod;
import mspets.my.zoomod.client.entities.models.CrocodileModel;
import mspets.my.zoomod.common.entities.CrocodileEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CrocodileRenderer<Type extends CrocodileEntity> extends MobRenderer<Type, CrocodileModel<Type>>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(MyZooMod.MODID, "textures/entities/crocodile.png");

    public CrocodileRenderer(EntityRendererProvider.Context context)
    {
        super(context, new CrocodileModel(context.bakeLayer(CrocodileModel.LAYER_LOCATION)), 1f);
    }

    @Override
    public ResourceLocation getTextureLocation(CrocodileEntity pEntity)
    {
        return TEXTURE;
    }
}
