package net.roxeez.advancement.display;

import com.google.common.base.Preconditions;
import com.google.common.reflect.Reflection;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Represent an advancement icon
 */
public class Icon {

    /**
     * Key of displayed item in this icon
     */
    @Expose
    @SerializedName("item")
    private NamespacedKey item;
    
    /**
     * NBT values of displayed item in this icon
     */
    @Expose
    @SerializedName("nbt")
    private String nbt;
    
    public Icon() {
    }
    
    public Icon(@NotNull NamespacedKey item, String nbt) {
        Preconditions.checkNotNull(item);
        this.item = item;
        this.nbt = nbt;
    }
    
    public Icon(@NotNull Material material, String nbt) {
        Preconditions.checkNotNull(material);
        this.item = material.getKey();
        this.nbt = nbt;
    }

    public Icon(ItemStack item) {
        this.item = item.getType().getKey();
        //please don't look at the following code
        try {
            String packageName = Bukkit.getServer().getClass().getPackageName();
            Class<?> aClass = Class.forName(packageName + ".inventory.CraftItemStack");
            Method asNMSCopy = aClass.getMethod("asNMSCopy", ItemStack.class);
            Object nmsstack = asNMSCopy.invoke(null, item);
            Class<?> nmsItemStack = nmsstack.getClass();
            Field f = null;
            for (Field field : nmsItemStack.getDeclaredFields()) {
                if (field.getType().getSimpleName().equals("NBTTagCompound")) {
                    f = field;
                    break;
                }
            }
            if (f == null) {
                this.nbt = null;
                return;
            }
            Object compound = f.get(nmsstack);
            this.nbt = compound == null ? null : compound.toString();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Set item shown in this icon
     *
     * @param material Material to display in this icon
     */
    public void setItem(@NotNull Material material) {
        Preconditions.checkNotNull(material);
        this.item = material.getKey();
    }
    
    /**
     * Set the NBT values of the icon
     * 
     * @param nbt NBT values of the displayed icon
     */
    public void setNbt(String nbt) {
        this.nbt = nbt;
    }
    
}
