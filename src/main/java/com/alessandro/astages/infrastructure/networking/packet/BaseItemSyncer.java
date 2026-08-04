package com.alessandro.astages.infrastructure.networking.packet;

import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.nullability.NotNullParams;

@NotNullParams
@Info("TO BE REMOVED")
public abstract class BaseItemSyncer extends BaseRestrictionSyncer {
//    private final boolean renderItemName;
//    private final boolean hideTooltip;
//    private final boolean hideInRecipeViewer;
//
//    public BaseItemSyncer(String id, String stage, boolean renderItemName, boolean hideTooltip, boolean hideInRecipeViewer) {
//        super(id, stage);
//        this.renderItemName = renderItemName;
//        this.hideTooltip = hideTooltip;
//        this.hideInRecipeViewer = hideInRecipeViewer;
//    }
//
//    public BaseItemSyncer(FriendlyByteBuf buf) {
//        super(buf);
//        renderItemName = buf.readBoolean();
//        hideTooltip = buf.readBoolean();
//        hideInRecipeViewer = buf.readBoolean();
//    }
//
//    @Override
//    public void toBytes(FriendlyByteBuf buf) {
//        super.toBytes(buf);
//        buf.writeBoolean(renderItemName);
//        buf.writeBoolean(hideTooltip);
//        buf.writeBoolean(hideInRecipeViewer);
//    }
//
//    public boolean isRenderItemName() {
//        return renderItemName;
//    }
//
//    public boolean isHideTooltip() {
//        return hideTooltip;
//    }
//
//    public boolean isHideInJei() {
//        return hideInRecipeViewer;
//    }
}