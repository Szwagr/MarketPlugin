/*    */ package me.szwagru.market;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import me.szwagru.market.settings.settings;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ public class MarketItemClass
/*    */ {
/* 10 */   public static List<MarketItem> marketItems = new ArrayList<>();
/*    */   public static void addMarketItem(ItemStack sellingItem, ItemStack costItem, String owner) {
/* 12 */     MarketItem marketItem = new MarketItem(sellingItem, costItem, owner);
/* 13 */     marketItem.lore = marketItem.getSellingItem().getItemMeta().getLore();
/* 14 */     marketItems.add(marketItem);
/*    */   }
/*    */   
/*    */   public static class MarketItem
/*    */   {
/*    */     private ItemStack sellingItem;
/*    */     private ItemStack costItem;
/*    */     private String owner;
/*    */     public int time;
/* 23 */     public List<String> lore = new ArrayList<>();
/*    */     public MarketItem(ItemStack sellingItem, ItemStack costItem, String owner) {
/* 25 */       this.sellingItem = sellingItem;
/* 26 */       this.costItem = costItem;
/* 27 */       this.owner = owner;
/* 28 */       this.time = settings.get().getInt("time");
/*    */     }

    public MarketItem(ItemStack sellingItem, ItemStack costItem, String owner, int time) {
    }

    /*    */ 
/*    */     
/*    */     public ItemStack getSellingItem() {
/* 33 */       return this.sellingItem;
/*    */     }
/*    */     
/*    */     public void setSellingItem(ItemStack sellingItem) {
/* 37 */       this.sellingItem = sellingItem;
/*    */     }
/*    */     
/*    */     public ItemStack getCostItem() {
/* 41 */       return this.costItem;
/*    */     }
/*    */     
/*    */     public void setCostItem(ItemStack costItem) {
/* 45 */       this.costItem = costItem;
/*    */     }
/*    */     
/*    */     public String getOwner() {
/* 49 */       return this.owner;
/*    */     }
/*    */     
/*    */     public void setOwner(String owner) {
/* 53 */       this.owner = owner;
/*    */     }
/*    */   }
/*    */ }