package com.example.artillery.entity;

import com.example.artillery.ArtilleryConfig;
import com.example.artillery.explosion.ArtilleryBlast;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraftforge.network.NetworkHooks;

public class ArtilleryShellEntity extends Entity {
    private Vec3 vel = Vec3.ZERO;
    private boolean armed = true;

    public ArtilleryShellEntity(EntityType<? extends ArtilleryShellEntity> type, Level lvl){
        super(type, lvl);
        this.noPhysics = true;
    }

    public void init(Vec3 velocity){
        this.vel = velocity;
    }

    @Override
    public void tick(){
        super.tick();
        if (!level().isClientSide) this.setPos(this.getX(), this.getY(), this.getZ()); // ensure server auth

        Vec3 oldPos = position();
        if (!this.isAlive()) return;

        // integrate physics
        double dt = ArtilleryConfig.DT;
        double h = getY();
        double rho = ArtilleryConfig.RHO0 * Math.exp(-h / ArtilleryConfig.SCALE_H);
        double g = ArtilleryConfig.G0 * Math.pow(ArtilleryConfig.RE/(ArtilleryConfig.RE+h),2);
        double v = vel.length();
        if (v > 0) {
            double A = Math.PI * (ArtilleryConfig.DIAMETER*ArtilleryConfig.DIAMETER) * 0.25;
            double drag = 0.5 * rho * ArtilleryConfig.CD * A * v * v / ArtilleryConfig.MASS;
            Vec3 dragAcc = vel.normalize().scale(-drag);
            Vec3 gravAcc = new Vec3(0,-g,0);
            Vec3 acc = dragAcc.add(gravAcc);
            vel = vel.add(acc.scale(dt));
        } else {
            vel = vel.add(0, -g*dt, 0);
        }

        Vec3 newPos = oldPos.add(vel.scale(dt));

        // raytrace for block hit
        ClipContext ctx = new ClipContext(oldPos, newPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this);
        BlockHitResult bhr = level().clip(ctx);
        if (bhr.getType() != HitResult.Type.MISS) {
            newPos = bhr.getLocation();
            explode(newPos);
            return;
        }

        // entity hit
        HitResult entHit = ProjectileUtil.getHitResultOnMoveVector(this, oldPos, newPos,
            this.getBoundingBox().expandTowards(vel.scale(dt)).inflate(1.0), e->e.isPickable(), 0);
        if (entHit != null && entHit.getType()!=HitResult.Type.MISS) {
            newPos = entHit.getLocation();
            explode(newPos);
            return;
        }

        setPos(newPos.x, newPos.y, newPos.z);

        if (this.getY() <= level().getMinBuildHeight()-5) {
            explode(newPos);
        }
    }

    private void explode(Vec3 pos){
        ArtilleryBlast.explode(level(), pos);
        discard();
    }

    @Override protected void defineSynchedData(){{}}
    @Override protected void readAdditionalSaveData(CompoundTag tag){{}}
    @Override protected void addAdditionalSaveData(CompoundTag tag){{}}

    @Override
    public Packet<?> getAddEntityPacket() {{
        return NetworkHooks.getEntitySpawningPacket(this);
    }}
}
