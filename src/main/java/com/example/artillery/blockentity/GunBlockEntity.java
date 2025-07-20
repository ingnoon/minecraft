package com.example.artillery.blockentity;

import com.example.artillery.ArtilleryConfig;
import com.example.artillery.menu.ArtilleryMenu;
import com.example.artillery.physics.BallisticsSolver;
import com.example.artillery.registry.ModBlockEntities;
import com.example.artillery.registry.ModEntities;
import com.example.artillery.entity.ArtilleryShellEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public final class GunBlockEntity extends BlockEntity {
    private int muzzleVel;
    private BlockPos targetPos;
    private BallisticsSolver.Pair cachedPair;
    private long lastSolveGameTime;
    private int cooldownTicks; // 10s=200 ticks -> config? constant

    public static final int FIRE_COOLDOWN_TICKS = 200; // 10s

    public GunBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.GUN_BLOCK_ENTITY, pos, state);
    }

    public GunBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.muzzleVel = ArtilleryConfig.DEFAULT_MUZZLE_VEL;
    }

    // getters/setters -----------------------------------------------------
    public int getMuzzleVel() { return muzzleVel; }
    public void setMuzzleVel(int v) {
        v = Math.max(50, Math.min(5000, v));
        this.muzzleVel = v; setChanged();
    }

    @Nullable public BlockPos getTarget() { return targetPos; }
    public void setTarget(@Nullable BlockPos p) { targetPos = p; setChanged(); }

    public boolean isCoolingDown(){ return cooldownTicks > 0; }
    public int getCooldownTicks(){ return cooldownTicks; }

    public BallisticsSolver.Pair getCachedPair(){ return cachedPair; }

    public Vec3 muzzlePos(){
        return new Vec3(worldPosition.getX()+0.5, worldPosition.getY()+1.0, worldPosition.getZ()+0.5);
    }

    // solving -------------------------------------------------------------
    public void solveAndCache(){
        if (level == null || targetPos == null) return;
        Vec3 gun = muzzlePos();
        Vec3 tgt = new Vec3(targetPos.getX()+0.5, targetPos.getY()+0.0, targetPos.getZ()+0.5);
        double dx = tgt.x - gun.x;
        double dz = tgt.z - gun.z;
        double horiz = Math.sqrt(dx*dx + dz*dz);
        double elev0 = gun.y;
        double elevT = tgt.y;
        cachedPair = BallisticsSolver.solvePair(elev0, elevT, horiz, muzzleVel);
        lastSolveGameTime = level.getGameTime();
    }

    // fire ----------------------------------------------------------------
    public void tryFire(Player requester, boolean useHighAngle){
        if (level == null || level.isClientSide) return;
        if (isCoolingDown()) return;
        if (targetPos == null) return;
        if (cachedPair == null) solveAndCache();

        BallisticsSolver.Solution sol = useHighAngle && cachedPair.high()!=null ?
            cachedPair.high() : cachedPair.low()!=null ? cachedPair.low() : null;
        if (sol == null) return;

        // compute direction vector (yaw from target, pitch from angle)
        Vec3 gun = muzzlePos();
        Vec3 tgt = new Vec3(targetPos.getX()+0.5, targetPos.getY()+0.0, targetPos.getZ()+0.5);
        double dx = tgt.x - gun.x;
        double dz = tgt.z - gun.z;
        double az = Math.atan2(dz, dx); // yaw
        double ang = sol.angleRad();
        double vx = muzzleVel * Math.cos(ang) * Math.cos(az);
        double vz = muzzleVel * Math.cos(ang) * Math.sin(az);
        double vy = muzzleVel * Math.sin(ang);
        Vec3 vel = new Vec3(vx, vy, vz);

        ServerLevel sl = (ServerLevel)level;
        ArtilleryShellEntity shell = new ArtilleryShellEntity(ModEntities.ARTILLERY_SHELL, sl);
        shell.setPos(gun.x, gun.y, gun.z);
        shell.init(vel);
        sl.addFreshEntity(shell);

        cooldownTicks = FIRE_COOLDOWN_TICKS;
        setChanged();
    }

    // tick ----------------------------------------------------------------
    public static void serverTick(Level lvl, BlockPos pos, BlockState state, GunBlockEntity gun){
        if (gun.cooldownTicks>0) gun.cooldownTicks--;
    }

    // persistence ---------------------------------------------------------
    @Override
    public void load(CompoundTag tag){
        super.load(tag);
        muzzleVel = tag.getInt("MuzzleVel");
        cooldownTicks = tag.getInt("Cooldown");
        if (tag.contains("Target")) targetPos = BlockPos.of(tag.getLong("Target"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag){
        super.saveAdditional(tag);
        tag.putInt("MuzzleVel", muzzleVel);
        tag.putInt("Cooldown", cooldownTicks);
        if (targetPos != null) tag.putLong("Target", targetPos.asLong());
    }

    // menu helper ---------------------------------------------------------
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player ply){
        return new ArtilleryMenu(id, inv, this);
    }
}
