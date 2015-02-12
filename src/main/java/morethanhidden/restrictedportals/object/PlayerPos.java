package morethanhidden.restrictedportals.object;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PlayerPos{
 public int dim;
 public float pitch;
 public float yaw;
 public double posX;
 public double posY;
 public double posZ;
 
	public PlayerPos(double x, double y, double z)
	    {
	        this.posX = x;
	        this.posY = y;
	        this.posZ = z;
	    }
	
	public PlayerPos(Entity entity)
    {	
		this.dim = entity.dimension;
        this.posX = entity.posX;
        this.posY = entity.posY;
        this.posZ = entity.posZ;
        this.pitch = entity.rotationPitch;
        this.yaw = entity.rotationYaw;
    }
	
	public int getDim()
    {
        return dim;
    }

    public double getX()
    {
        return posX;
    }

    public double getY()
    {
        return posY;
    }

    public double getZ()
    {
        return posZ;
    }
    public float getPitch()
    {
        return pitch;
    }

    public float getYaw()
    {
        return yaw;
    }
	
}