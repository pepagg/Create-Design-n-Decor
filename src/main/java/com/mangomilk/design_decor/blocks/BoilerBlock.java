package com.mangomilk.design_decor.blocks;

import com.google.common.base.Predicates;
import com.mangomilk.design_decor.registry.MmbBlocks;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.foundation.placement.PoleHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Predicate;

public class BoilerBlock extends WrenchableDirectionalBlock {

    public static final int placementHelperId = PlacementHelpers.register(new PlacementHelper());
    public BoilerBlock(Properties p_52591_) {
        super(p_52591_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    public static final VoxelShape SHAPE = Block.box(-8.0D, -8.0D, -8.0D, 24.0D, 24.0D, 24.0D);



  public VoxelShape getOcclusionShape(BlockState p_60578_, BlockGetter p_60579_, BlockPos p_60580_) {
      return SHAPE;
  }



    public RenderShape getRenderShape(BlockState p_54559_) {
        return RenderShape.MODEL;
    }


    public boolean useShapeForLightOcclusion(BlockState p_54582_) {
        return true;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55125_) {
        p_55125_.add(FACING);
    }
    public BlockState getStateForPlacement(BlockPlaceContext p_55087_) {
        return this.defaultBlockState().setValue(FACING, p_55087_.getNearestLookingDirection().getOpposite().getOpposite());
    }


    @MethodsReturnNonnullByDefault
    private static class PlacementHelper extends PoleHelper<Direction> {


        private PlacementHelper() {
            super(state -> state.getBlock() instanceof BoilerBlock, state -> state.getValue(FACING).getAxis(), FACING);
        }

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return i -> i.getItem() instanceof BlockItem
                    && ((BlockItem) i.getItem()).getBlock() instanceof BoilerBlock;
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return Predicates.or(
                MmbBlocks.ALUMINUM_BOILER::has,
                MmbBlocks.ALUMINUM_BOILER_SPECIAL::has,
                MmbBlocks.ZINC_BOILER::has,
                MmbBlocks.CAST_IRON_BOILER::has,
                MmbBlocks.GOLD_BOILER::has,
                MmbBlocks.INDUSTRIAL_IRON_BOILER::has,
                MmbBlocks.BRASS_BOILER::has,
                MmbBlocks.COPPER_BOILER::has,
                MmbBlocks.CAPITALISM_BOILER::has,
                MmbBlocks.ANDESITE_BOILER::has
            );
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos,
                                         BlockHitResult ray) {
            PlacementOffset offset = super.getOffset(player, world, state, pos, ray);
            if (offset.isSuccessful())
                offset.withTransform(offset.getTransform()
                        .andThen(s -> pickCorrectBoiler(s, world, offset.getBlockPos())));
            return offset;
        }

    }
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
                                 BlockHitResult pHit) {
        if (pPlayer == null)
            return InteractionResult.PASS;

        ItemStack itemInHand = pPlayer.getItemInHand(pHand);

        IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
        if (helper.matchesItem(itemInHand))
            return helper.getOffset(pPlayer, pLevel, pState, pPos, pHit)
                    .placeInWorld(pLevel, (BlockItem) itemInHand.getItem(), pPlayer, pHand, pHit);

        return InteractionResult.PASS;
    }
    public static BlockState pickCorrectBoiler(BlockState stateForPlacement, Level level, BlockPos pos) {
        //if (PoweredShaftBlock.stillValid(stateForPlacement, level, pos))
        //    return PoweredShaftBlock.getEquivalent(stateForPlacement);
        return stateForPlacement;
    }



}
