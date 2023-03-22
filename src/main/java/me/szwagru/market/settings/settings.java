/*    */ package me.szwagru.market.settings;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import org.bukkit.configuration.file.YamlConfiguration;
/*    */ 
/*    */ 
/*    */ public class settings
/*    */ {
/*    */   private static File file;
/*    */   private static FileConfiguration settings;
/*    */   
/*    */   public static void setup() {
/* 16 */     file = new File(Bukkit.getServer().getPluginManager().getPlugin("MarketPlugin").getDataFolder(), "settings.yml");
/* 17 */     if (!file.exists()) {
/*    */       
/*    */       try {
/* 20 */         file.createNewFile();
/* 21 */       } catch (IOException iOException) {}
/*    */     }
/*    */ 
/*    */     
/* 25 */     settings = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static FileConfiguration get() {
/* 31 */     return settings;
/*    */   }
/*    */   
/*    */   public static void save() {
/*    */     try {
/* 36 */       settings.save(file);
/* 37 */     } catch (IOException e) {
/*    */       
/* 39 */       System.out.println("[Trade Market] Can't save the file. Contact with the owner to fix your issue Szwagru#3201");
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static void reload() {
/* 45 */     settings = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void defaultsetup() {
/* 50 */     get().addDefault("time", Integer.valueOf(60));
/* 51 */     get().addDefault("max_items", Integer.valueOf(9));
/* 52 */     get().addDefault("max_amount_of_item_sell", Integer.valueOf(999));
/*    */   }
/*    */ }