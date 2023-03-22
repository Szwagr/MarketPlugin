/*    */ package me.szwagru.market.lang;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import org.bukkit.configuration.file.YamlConfiguration;
/*    */ 
/*    */ 
/*    */ public class lang
/*    */ {
/*    */   private static File file;
/*    */   private static FileConfiguration lang;
/*    */   
/*    */   public static void setup() {
/* 16 */     file = new File(Bukkit.getServer().getPluginManager().getPlugin("MarketPlugin").getDataFolder(), "lang.yml");
/* 17 */     if (!file.exists()) {
/*    */       
/*    */       try {
/* 20 */         file.createNewFile();
/* 21 */       } catch (IOException iOException) {}
/*    */     }
/*    */ 
/*    */     
/* 25 */     lang = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static FileConfiguration get() {
/* 31 */     return lang;
/*    */   }
/*    */   
/*    */   public static void save() {
/*    */     try {
/* 36 */       lang.save(file);
/* 37 */     } catch (IOException e) {
/*    */       
/* 39 */       System.out.println("[Trade Market] Can't save the file. Contact with the owner to fix your issue sizeof#2792");
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static void reload() {
/* 45 */     lang = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void defaultsetup() {
/* 50 */     get().addDefault("help-1", "&a /trademarket sell <item id> <item amount> - Sells item in main hand for item id and item amount in arguments");
/* 51 */     get().addDefault("help-2", "&a /trademarket receive - Here player can take items");
/* 52 */     get().addDefault("help-3", "&a /trademarket - Opens market");
/* 53 */     get().addDefault("help-4", "&a /trademarket info - information about plugin");
/* 54 */     get().addDefault("error-1", "&4 You must hold an item to sell it on the market.");
/* 55 */     get().addDefault("error-2", "&4You have max amount of items on sale");
/* 56 */     get().addDefault("error-3", "&4You have no items to receive");
/* 57 */     get().addDefault("error-4", "&4You cant buy own item");
/* 58 */     get().addDefault("message-1", "&f added to market for");
/* 59 */     get().addDefault("message-2", "&f You have successfully purchased");
/* 60 */     get().addDefault("message-3", "&f for");
/* 61 */     get().addDefault("message-4", "&f Someone bought your item! Receive it by typing /trademarket receive");
/* 62 */     get().addDefault("error-5", "&4 You do not have enough");
/* 63 */     get().addDefault("message-5", "&4 to purchase");
/* 64 */     get().addDefault("message-6", "&4 You have items in your receive section check it out!");
/* 65 */     get().addDefault("error-6", "&4 Value is too big!");
             get().addDefault("error-7", "&4Cena nie może być równa &4&l0&r &4!");
             get().addDefault("message-7", "& Pomyślnie usunołeś item z marketu");
/*    */   }
/*    */ }


/* Location:              C:\Users\Kacper\Downloads\TradeMarket-1.0-SNAPSHOT.jar!\me\gujix64\trademarket\lang\lang.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */