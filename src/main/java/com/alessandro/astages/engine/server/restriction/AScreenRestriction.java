package com.alessandro.astages.engine.server.restriction;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.api.restriction.ARestriction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.TriPredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@NotNullParamsAndMethodsReturn
public class AScreenRestriction extends ARestriction<AScreenRestriction, MenuType<?>, AbstractContainerMenu> {
    private final List<MenuType<?>> menus = new ArrayList<>();

    private TriPredicate<BlockState, BlockEntity, AbstractContainerMenu> checker;

    public List<MenuType<?>> getMenus() {
        return menus;
    }

    public AScreenRestriction(String id, String stage) {
        super(id, stage);
    }

    @SuppressWarnings("unused")
    public static AScreenRestriction newBuilder() {
        return new AScreenRestriction("null", "null");
    }

    @Override
    public AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.Screen.OPEN_MESSAGE)
            .addAttribute(Attributes.HAS_CHECKER);

        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withSelf(defaultAttributes)
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, AScreenRestriction.class)
            .build();
    }

    @Override
    public AScreenRestriction restrict(MenuType<?> menu) {
        menus.add(menu);

        return this;
    }

    @Override
    public boolean isRestricted(AbstractContainerMenu menu) {
        var type = menu.getType();

        for (MenuType<?> men : menus) {
            if (men.equals(type)) {
                return true;
            }
        }

        return false;
    }

    public boolean isRestricted(AbstractContainerMenu menu, @Nullable BlockState state, @Nullable BlockEntity entity) {
        if (isDisabled(Attributes.HAS_CHECKER)) { return isRestricted(menu); }

        var type = menu.getType();
        for (MenuType<?> men : menus) {
            if (men.equals(type) && checker.test(state, entity, menu)) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("unused")
    public AScreenRestriction setOpenMessage(Function<MenuType<?>, Component> message) {
        set(Attributes.Screen.OPEN_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public AScreenRestriction setChecker(TriPredicate<BlockState, BlockEntity, AbstractContainerMenu> checker) {
        this.checker = checker;
        set(Attributes.HAS_CHECKER, true);
        return this;
    }

    public TriPredicate<BlockState, BlockEntity, AbstractContainerMenu> getChecker() {
        return checker;
    }
}
