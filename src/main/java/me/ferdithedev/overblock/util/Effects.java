package me.ferdithedev.overblock.util;

import jline.internal.Nullable;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.util.Vector;

import java.util.List;

public class Effects {

    public static void playSoundDistance(Location location, double distance, Sound sound, float volume, float pitch) {
        if(location.getWorld() != null) {
            location.getWorld().getPlayers().stream().filter(p->location.distance(p.getLocation()) < distance).forEach(p-> location.getWorld().playSound(p,sound, SoundCategory.MASTER,volume,pitch));
        }
    }

    public static class ParticleEffectPart {

        private final Particle particle;
        private final int count;
        private final double offsetX;
        private final double offsetY;
        private final double offsetZ;
        private final Object data;
        private final double extra;

        public <T> ParticleEffectPart(Particle particle, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data, double extra) {
            this.particle = particle;
            this.count = count;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;

            this.data = data;

            this.extra = extra;
        }

        public void spawn(Location loc) {
            if(loc.getWorld() == null) return;
            loc.getWorld().spawnParticle(particle,loc,count,offsetX,offsetY,offsetZ,extra,data);
        }
    }

    public record ParticleEffect(List<ParticleEffectPart> effectParts) {
        public void spawn(Location location) {
            for(ParticleEffectPart particle : effectParts) {
                particle.spawn(location);
            }
        }
    }

    public static void drawParticleLine(Location loc1, Location loc2, ParticleEffect effect, double stepDistance) {
        Vector vector = loc2.toVector().subtract(loc1.toVector());
        for(double i = 1; i <= loc1.distance(loc2); i += stepDistance) {
            vector.multiply(i);
            loc1.add(vector);
            effect.spawn(loc1);
            loc1.subtract(vector);
            vector.normalize();
        }
    }

    public static void drawParticleLine(Location loc1, Location loc2, ParticleEffectPart effect, double stepDistance) {
        Vector vector = loc2.toVector().subtract(loc1.toVector());
        for(double i = 1; i <= loc1.distance(loc2); i += stepDistance) {
            vector.multiply(i);
            loc1.add(vector);
            effect.spawn(loc1);
            loc1.subtract(vector);
            vector.normalize();
        }
    }

    public static Vector getDirectionBetweenLocations(Location Start, Location End) {
        Vector from = Start.toVector();
        Vector to = End.toVector();
        return to.subtract(from);
    }
}
