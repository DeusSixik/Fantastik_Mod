package net.lisalaf.fantastikmod.worldgen.tree;

import net.lisalaf.fantastikmod.block.ModBlocks;
import net.lisalaf.fantastikmod.block.custom.MoonSaplingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BigMoonTreeGrower {

    public static boolean growBigMoonTree(LevelAccessor level, BlockPos pos, RandomSource random) {
        for (int x = 0; x < 2; x++) {
            for (int z = 0; z < 2; z++) {
                BlockPos belowPos = pos.offset(x, -1, z);
                if (level.getFluidState(belowPos).is(FluidTags.WATER)) {
                    return false;
                }
            }
        }

        for (int x = 0; x < 2; x++) {
            for (int z = 0; z < 2; z++) {
                BlockPos checkPos = pos.offset(x, 0, z);
                if (!(level.getBlockState(checkPos).getBlock() instanceof MoonSaplingBlock)) {
                    return false;
                }
            }
        }

        removeSaplings(level, pos);

        int height = 32 + random.nextInt(8);

        if (!checkSpace(level, pos, height)) {
            restoreSaplings(level, pos);
            return false;
        }

        generateTrunk(level, pos, height, random);
        generateRoots(level, pos, random);
        generateBranches(level, pos, height, random);
        generateVerticalBranches(level, pos, height, random);
        generateCanopy(level, pos, height, random);

        return true;
    }

    private static void removeSaplings(LevelAccessor level, BlockPos pos) {
        for (int x = 0; x < 2; x++) {
            for (int z = 0; z < 2; z++) {
                level.setBlock(pos.offset(x, 0, z), Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }

    private static void restoreSaplings(LevelAccessor level, BlockPos pos) {
        for (int x = 0; x < 2; x++) {
            for (int z = 0; z < 2; z++) {
                level.setBlock(pos.offset(x, 0, z), ModBlocks.MOON_SAPLING.get().defaultBlockState(), 3);
            }
        }
    }

    public static boolean generateBigMoonTree(WorldGenLevel level, BlockPos pos, RandomSource random) {
        for (int x = 0; x < 2; x++) {
            for (int z = 0; z < 2; z++) {
                BlockPos belowPos = pos.offset(x, -1, z);
                if (level.getFluidState(belowPos).is(FluidTags.WATER)) {
                    return false;
                }
            }
        }

        int height = 32 + random.nextInt(8);

        if (!checkSpaceForWorldGen(level, pos, height)) {
            return false;
        }

        generateTrunk(level, pos, height, random);
        generateRoots(level, pos, random);
        generateBranches(level, pos, height, random);
        generateVerticalBranches(level, pos, height, random);
        generateCanopy(level, pos, height, random);

        return true;
    }


    private static boolean checkSpaceForWorldGen(WorldGenLevel level, BlockPos pos, int height) {
        for (int y = 0; y <= height + 3; y++) {
            for (int x = -1; x <= 2; x++) {
                for (int z = -1; z <= 2; z++) {
                    BlockPos checkPos = pos.offset(x, y, z);
                    BlockState state = level.getBlockState(checkPos);
                    if (!state.isAir() && !state.canBeReplaced()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static boolean checkSpace(LevelAccessor level, BlockPos pos, int height) {
        for (int y = 0; y <= height + 3; y++) {
            for (int x = -1; x <= 2; x++) {
                for (int z = -1; z <= 2; z++) {
                    BlockPos checkPos = pos.offset(x, y, z);
                    BlockState state = level.getBlockState(checkPos);
                    if (!state.isAir() && !state.is(ModBlocks.MOON_SAPLING.get()) && !state.canBeReplaced()) {
                        return false;
                    }
                }
            }
        }

        int crownStart = height - 12;
        int radius = 8;
        for (int y = crownStart; y <= height + 4; y++) {
            for (int x = -radius; x <= radius + 2; x++) {
                for (int z = -radius; z <= radius + 2; z++) {
                    BlockPos checkPos = pos.offset(x, y, z);
                    if (!level.getBlockState(checkPos).isAir() && y > height - 3) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    private static void generateTrunk(LevelAccessor level, BlockPos pos, int height, RandomSource random) {
        for (int y = 0; y <= height; y++) {
            for (int x = 0; x < 2; x++) {
                for (int z = 0; z < 2; z++) {
                    BlockPos trunkPos = pos.offset(x, y, z);
                    BlockState state = level.getBlockState(trunkPos);
                    if (state.isAir() || state.is(ModBlocks.MOON_SAPLING.get())) {
                        level.setBlock(trunkPos, ModBlocks.TREE_MOON_LOG_BLOCK.get().defaultBlockState(), 3);
                    }
                }
            }
        }


        for (int y = -2; y <= 3; y++) {
            for (int x = -1; x <= 2; x++) {
                for (int z = -1; z <= 2; z++) {
                    if ((x == -1 || x == 2) && (z == -1 || z == 2)) continue;
                    BlockPos thickPos = pos.offset(x, y, z);
                    if (level.getBlockState(thickPos).isAir()) {
                        level.setBlock(thickPos, ModBlocks.TREE_MOON_LOG_BLOCK.get().defaultBlockState(), 3);
                    }
                }
            }
        }
        for (int y = height + 1; y <= height + 2; y++) {
            BlockPos topPos = pos.offset(0, y, 0);
            if (level.getBlockState(topPos).isAir()) {
                level.setBlock(topPos, ModBlocks.TREE_MOON_LOG_BLOCK.get().defaultBlockState(), 3);
            }
        }
    }

    private static void generateRoots(LevelAccessor level, BlockPos pos, RandomSource random) {
        int rootLength = 6 + random.nextInt(4);
        Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
        for (Direction dir : directions) {
            for (int i = 1; i <= rootLength; i++) {
                int yOffset = -1;
                if (i == 1) yOffset = 0;
                if (i >= 4) yOffset = -2;
                if (i >= 6) yOffset = -3;

                BlockPos rootPos = pos.offset(dir.getStepX() * i, yOffset, dir.getStepZ() * i);
                if (level.getBlockState(rootPos).isAir() || level.getBlockState(rootPos).is(Blocks.DIRT) || level.getBlockState(rootPos).is(Blocks.GRASS_BLOCK)) {
                    level.setBlock(rootPos, ModBlocks.TREE_MOON_WOOD.get().defaultBlockState(), 3);
                }

                if (i == 1) {
                    for (int w = -1; w <= 1; w++) {
                        for (int h = -1; h <= 0; h++) {
                            BlockPos thickPos = rootPos.offset(w, h, w);
                            if (level.getBlockState(thickPos).isAir() && (w != 0 || h != 0)) {
                                level.setBlock(thickPos, ModBlocks.TREE_MOON_WOOD.get().defaultBlockState(), 3);
                            }
                        }
                    }
                }
            }

            for (int i = 2; i <= rootLength - 1; i++) {
                if (random.nextFloat() < 0.6f) {
                    Direction sideDir = random.nextBoolean() ? dir.getClockWise() : dir.getCounterClockWise();
                    for (int s = 1; s <= 2; s++) {
                        BlockPos sideRoot = pos.offset(dir.getStepX() * i + sideDir.getStepX() * s, -1 - (s/2), dir.getStepZ() * i + sideDir.getStepZ() * s);
                        if (level.getBlockState(sideRoot).isAir()) {
                            level.setBlock(sideRoot, ModBlocks.TREE_MOON_WOOD.get().defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
        int[] diagX = {1, 1, -1, -1};
        int[] diagZ = {1, -1, 1, -1};

        for (int d = 0; d < 4; d++) {
            for (int i = 1; i <= 5; i++) {
                int yOffset = -1;
                if (i == 1) yOffset = 0;
                if (i >= 4) yOffset = -2;

                BlockPos rootPos = pos.offset(diagX[d] * i, yOffset, diagZ[d] * i);
                if (level.getBlockState(rootPos).isAir() || level.getBlockState(rootPos).is(Blocks.DIRT) || level.getBlockState(rootPos).is(Blocks.GRASS_BLOCK)) {
                    level.setBlock(rootPos, ModBlocks.TREE_MOON_WOOD.get().defaultBlockState(), 3);
                }
            }
        }
        for (int x = -2; x <= 3; x++) {
            for (int z = -2; z <= 3; z++) {
                if (random.nextFloat() < 0.5f) {
                    int depth = 2 + random.nextInt(4);
                    for (int d = 1; d <= depth; d++) {
                        BlockPos rootPos = pos.offset(x, -d, z);
                        if (level.getBlockState(rootPos).isAir() || level.getBlockState(rootPos).is(Blocks.DIRT) || level.getBlockState(rootPos).is(Blocks.GRASS_BLOCK)) {
                            level.setBlock(rootPos, ModBlocks.TREE_MOON_WOOD.get().defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
    }

    private static void generateBranches(LevelAccessor level, BlockPos pos, int height, RandomSource random) {
        int branchCount = 14 + random.nextInt(8);
        int branchStartY = height - 14;

        for (int i = 0; i < branchCount; i++) {
            int branchHeight = branchStartY + random.nextInt(12);
            Direction branchDir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            int branchLength = 5 + random.nextInt(5);

            if (branchHeight < height - 14) continue;

            BlockPos startPos = pos.offset(0, branchHeight, 0);

            for (int l = 1; l <= branchLength; l++) {
                int yOffset = 0;
                if (l > 3) yOffset = -1;
                if (l > 5) yOffset = -2;

                BlockPos branchPos = startPos.offset(branchDir.getStepX() * l, yOffset + (random.nextInt(2) - 1), branchDir.getStepZ() * l);
                if (level.getBlockState(branchPos).isAir()) {
                    level.setBlock(branchPos, ModBlocks.TREE_MOON_LOG_BLOCK.get().defaultBlockState(), 3);
                }

                if (l > 2 && random.nextFloat() < 0.5f) {
                    Direction sideDir = random.nextBoolean() ? branchDir.getClockWise() : branchDir.getCounterClockWise();
                    int subLength = 2 + random.nextInt(3);
                    for (int s = 1; s <= subLength; s++) {
                        BlockPos subPos = branchPos.offset(sideDir.getStepX() * s, (s > 1 ? -1 : 0), sideDir.getStepZ() * s);
                        if (level.getBlockState(subPos).isAir()) {
                            level.setBlock(subPos, ModBlocks.TREE_MOON_LOG_BLOCK.get().defaultBlockState(), 3);
                            if (s == subLength) placeLeafCluster(level, subPos, random);
                        }
                    }
                }
            }

            BlockPos endPos = startPos.offset(branchDir.getStepX() * branchLength, -1, branchDir.getStepZ() * branchLength);
            placeLeafCluster(level, endPos, random);
        }
    }

    private static void generateVerticalBranches(LevelAccessor level, BlockPos pos, int height, RandomSource random) {
        int verticalBranchCount = 6 + random.nextInt(5);
        Direction[] dirs = {Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};

        for (int i = 0; i < verticalBranchCount; i++) {
            Direction startDir = dirs[random.nextInt(dirs.length)];
            int startY = 6 + random.nextInt(15);
            int endY = height - 10 + random.nextInt(8);

            if (startY >= endY) continue;

            BlockPos currentPos = pos.offset(startDir.getStepX(), startY, startDir.getStepZ());

            if (!level.getBlockState(currentPos).isAir()) continue;

            for (int y = startY; y <= endY; y++) {
                int xOffset = startDir.getStepX();
                int zOffset = startDir.getStepZ();

                if (random.nextFloat() < 0.3f) {
                    Direction sideDir = random.nextBoolean() ? startDir.getClockWise() : startDir.getCounterClockWise();
                    xOffset += sideDir.getStepX();
                    zOffset += sideDir.getStepZ();
                }

                BlockPos branchPos = pos.offset(xOffset, y, zOffset);
                if (level.getBlockState(branchPos).isAir()) {
                    level.setBlock(branchPos, ModBlocks.TREE_MOON_LOG_BLOCK.get().defaultBlockState(), 3);
                    currentPos = branchPos;

                    if (y > endY - 4 && random.nextFloat() < 0.6f) {
                        placeSmallLeafCluster(level, branchPos, random);
                    }
                } else {
                    break;
                }
            }

            placeSmallLeafCluster(level, currentPos, random);
        }

        int diagonalBranchCount = 8 + random.nextInt(6);

        for (int i = 0; i < diagonalBranchCount; i++) {
            int startY = 8 + random.nextInt(18);
            if (startY > height - 15) continue;

            Direction branchDir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            int branchLength = 4 + random.nextInt(5);

            BlockPos startPos = pos.offset(0, startY, 0);

            for (int l = 1; l <= branchLength; l++) {
                int yOffset = 1 + (l / 2);
                BlockPos branchPos = startPos.offset(branchDir.getStepX() * l, yOffset, branchDir.getStepZ() * l);
                if (level.getBlockState(branchPos).isAir()) {
                    level.setBlock(branchPos, ModBlocks.TREE_MOON_LOG_BLOCK.get().defaultBlockState(), 3);

                    if (random.nextFloat() < 0.4f && l > 2) {
                        Direction sideDir = random.nextBoolean() ? branchDir.getClockWise() : branchDir.getCounterClockWise();
                        BlockPos sidePos = branchPos.offset(sideDir.getStepX(), 0, sideDir.getStepZ());
                        if (level.getBlockState(sidePos).isAir()) {
                            level.setBlock(sidePos, ModBlocks.TREE_MOON_LOG_BLOCK.get().defaultBlockState(), 3);
                        }
                    }
                }
            }

            BlockPos endPos = startPos.offset(branchDir.getStepX() * branchLength, branchLength / 2, branchDir.getStepZ() * branchLength);
            placeSmallLeafCluster(level, endPos, random);
        }
    }

    private static void generateCanopy(LevelAccessor level, BlockPos pos, int height, RandomSource random) {
        int crownStart = height - 12;
        int maxRadius = 7;

        for (int y = crownStart; y <= height + 2; y++) {
            int radius;
            if (y <= height - 3) {
                radius = maxRadius;
            } else if (y <= height) {
                radius = maxRadius - 1;
            } else if (y == height + 1) {
                radius = maxRadius - 2;
            } else {
                radius = maxRadius - 3;
            }
            if (radius < 1) radius = 1;

            for (int x = -radius; x <= radius + 1; x++) {
                for (int z = -radius; z <= radius + 1; z++) {
                    BlockPos leafPos = pos.offset(x, y, z);

                    if (x >= 0 && x <= 1 && z >= 0 && z <= 1 && y <= height) {
                        continue;
                    }

                    double distance = Math.sqrt(x * x + z * z);
                    if (distance <= radius + 0.5) {
                        float density = 0.85f;
                        if (Math.abs(x) > radius - 1 || Math.abs(z) > radius - 1) density = 0.6f;
                        if (random.nextFloat() < density && level.getBlockState(leafPos).isAir()) {
                            level.setBlock(leafPos, ModBlocks.TREE_MOON_FOLIAGE_BLOCK.get().defaultBlockState(), 3);
                        }
                    }
                }
            }
        }

        for (int y = height + 1; y <= height + 4; y++) {
            int radius = 4 - (y - height - 1);
            if (radius < 0) radius = 0;

            for (int x = -radius; x <= radius + 1; x++) {
                for (int z = -radius; z <= radius + 1; z++) {
                    BlockPos leafPos = pos.offset(x, y, z);
                    double distance = Math.sqrt(x * x + z * z);
                    if (distance <= radius + 0.5 && level.getBlockState(leafPos).isAir()) {
                        level.setBlock(leafPos, ModBlocks.TREE_MOON_FOLIAGE_BLOCK.get().defaultBlockState(), 3);
                    }
                }
            }
        }

        for (int y = crownStart - 1; y <= crownStart + 2; y++) {
            for (int x = -maxRadius - 1; x <= maxRadius + 2; x++) {
                for (int z = -maxRadius - 1; z <= maxRadius + 2; z++) {
                    double distance = Math.sqrt(x * x + z * z);
                    if (distance >= maxRadius - 0.5 && distance <= maxRadius + 0.5) {
                        if (random.nextFloat() < 0.45f) {
                            BlockPos leafPos = pos.offset(x, y, z);
                            if (level.getBlockState(leafPos).isAir()) {
                                level.setBlock(leafPos, ModBlocks.TREE_MOON_FOLIAGE_BLOCK.get().defaultBlockState(), 3);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void placeLeafCluster(LevelAccessor level, BlockPos pos, RandomSource random) {
        int clusterSize = 3;
        for (int x = -clusterSize; x <= clusterSize; x++) {
            for (int z = -clusterSize; z <= clusterSize; z++) {
                for (int y = -2; y <= 2; y++) {
                    BlockPos leafPos = pos.offset(x, y, z);
                    double distance = Math.sqrt(x * x + y * y + z * z);
                    if (distance <= clusterSize + 0.5 && random.nextFloat() < 0.65f && level.getBlockState(leafPos).isAir()) {
                        level.setBlock(leafPos, ModBlocks.TREE_MOON_FOLIAGE_BLOCK.get().defaultBlockState(), 3);
                    }
                }
            }
        }
    }

    private static void placeSmallLeafCluster(LevelAccessor level, BlockPos pos, RandomSource random) {
        int clusterSize = 2;
        for (int x = -clusterSize; x <= clusterSize; x++) {
            for (int z = -clusterSize; z <= clusterSize; z++) {
                for (int y = -1; y <= 1; y++) {
                    BlockPos leafPos = pos.offset(x, y, z);
                    double distance = Math.sqrt(x * x + y * y + z * z);
                    if (distance <= clusterSize + 0.5 && random.nextFloat() < 0.7f && level.getBlockState(leafPos).isAir()) {
                        level.setBlock(leafPos, ModBlocks.TREE_MOON_FOLIAGE_BLOCK.get().defaultBlockState(), 3);
                    }
                }
            }
        }
    }
}