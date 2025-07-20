package com.example.artillery.explosion;

import com.example.artillery.ArtilleryConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public final class ArtilleryBlast {
    private ArtilleryBlast(){}

    public static void explode(Level lvl, Vec3 pos){
        explode(lvl,pos,
            ArtilleryConfig.BLAST_RADIUS,
            ArtilleryConfig.CASUALTY_RADIUS,
            (float)ArtilleryConfig.BASE_DAMAGE);
    }

    public static void explode(Level lvl, Vec3 pos, double blockR, double casualtyR, float baseDamage){
        if (lvl.isClientSide) return;

        // block destruction
        lvl.explode(null, pos.x, pos.y, pos.z, (float)blockR, Level.ExplosionInteraction.BLOCK);

        // entity damage falloff
        AABB box = new AABB(pos.x-casualtyR, pos.y-casualtyR, pos.z-casualtyR,
                            pos.x+casualtyR, pos.y+casualtyR, pos.z+casualtyR);
        List<LivingEntity> ents = lvl.getEntitiesOfClass(LivingEntity.class, box);
        for (LivingEntity e : ents) {
            double d = e.position().distanceTo(pos);
            if (d > casualtyR) continue;
            double exposure = Explosion.getSeenPercent(pos, e); // visibility
            double scale = Math.max(0.0, 1.0 - d/casualtyR);
            float dmg = (float)(baseDamage * exposure * scale);
            if (dmg > 0) e.hurt(lvl.damageSources().explosion(null, null), dmg);
        }
    }
}
