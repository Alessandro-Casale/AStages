package com.alessandro.astages.infrastructure.networking.packet;

import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.nullability.NotNullParams;

@NotNullParams
@Info("TO BE REMOVED")
public abstract class BaseItemSyncer extends BaseRestrictionSyncer {
//    private final boolean renderItemName;
//    private final boolean hideTooltip;
//    private final boolean hideInJei;
//
//    public BaseItemSyncer(String id, String stage, boolean renderItemName, boolean hideTooltip, boolean hideInJei) {
//        super(id, stage);
//        this.renderItemName = renderItemName;
//        this.hideTooltip = hideTooltip;
//        this.hideInJei = hideInJei;
//    }
//
//    public BaseItemSyncer(FriendlyByteBuf buf) {
//        super(buf);
//        renderItemName = buf.readBoolean();
//        hideTooltip = buf.readBoolean();
//        hideInJei = buf.readBoolean();
//    }
//
//    @Override
//    public void toBytes(FriendlyByteBuf buf) {
//        super.toBytes(buf);
//        buf.writeBoolean(renderItemName);
//        buf.writeBoolean(hideTooltip);
//        buf.writeBoolean(hideInJei);
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
//        return hideInJei;
//    }
}