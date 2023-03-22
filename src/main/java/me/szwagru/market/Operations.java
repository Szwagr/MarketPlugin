/*    */ package me.szwagru.market;
/*    */ 
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class Operations
/*    */ {
/*    */   public static boolean ValidateNumber(String arg1) {
/*    */     try {
/* 10 */       Integer.parseInt(arg1);
/* 11 */       return true;
/* 12 */     } catch (NumberFormatException var4) {
/* 13 */       return false;
/*    */     } 
/*    */   }
/*    */   public static boolean checkMaterial(String arg1, Player player) {
/* 17 */     if (Material.matchMaterial(arg1) != null) {
/* 18 */       return true;
/*    */     }
/* 20 */     player.sendMessage("Invalid item ID provided");
/* 21 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Kacper\Downloads\TradeMarket-1.0-SNAPSHOT.jar!\me\gujix64\trademarket\Operations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */