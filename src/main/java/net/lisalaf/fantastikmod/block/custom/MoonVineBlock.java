package net.lisalaf.fantastikmod.block.custom;

import net.lisalaf.fantastikmod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MoonVineBlock extends Block implements BonemealableBlock {
    public static final BooleanProperty TIP = BooleanProperty.create("tip");
    public static final VoxelShape SHAPE = box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);

    public MoonVineBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(TIP, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TIP);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
        return pState.getValue(TIP) && pLevel.getBlockState(pPos.below()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        BlockPos tipPos = pPos.below();
        if (pLevel.isEmptyBlock(tipPos)) {
            pLevel.setBlockAndUpdate(pPos, pState.setValue(TIP, false));
            pLevel.setBlockAndUpdate(tipPos, this.defaultBlockState().setValue(TIP, true));
            pLevel.sendBlockUpdated(pPos, pState, pState.setValue(TIP, false), 3);
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pState.canSurvive(pLevel, pPos)) {
            pLevel.destroyBlock(pPos, true);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos pos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        BlockPos above = pos.above();
        BlockPos below = pos.below();

        // Если сверху есть лоза ИЛИ снизу есть лоза — мы ТЕЛО
        if (level.getBlockState(above).is(this) || level.getBlockState(below).is(this)) {
            return this.defaultBlockState().setValue(TIP, false);
        }
        // Иначе — КОНЧИК
        return this.defaultBlockState().setValue(TIP, true);
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return pState.getValue(TIP);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (isAttachedToMoonFoliage(pLevel, pPos) && pRandom.nextFloat() < 0.1f) {
            performBonemeal(pLevel, pRandom, pPos, pState);
        }
    }

    private boolean isAttachedToMoonFoliage(Level level, BlockPos pos) {
        BlockPos checkPos = pos.above();
        while (level.getBlockState(checkPos).is(this)) {
            checkPos = checkPos.above();
        }
        return level.getBlockState(checkPos).is(ModBlocks.TREE_MOON_FOLIAGE_BLOCK.get());
    }
}