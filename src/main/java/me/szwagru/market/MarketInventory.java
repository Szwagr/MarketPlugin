package me.szwagru.market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;





public class MarketInventory
{
  public static List<String> createLore(MarketItemClass.MarketItem item, int currentitem) {
/*  21 */     List<String> lore = new ArrayList<>();
/*  22 */     lore.add(ChatColor.GREEN + "Cena: " + ChatColor.GRAY + item.getCostItem().getAmount() + "x " + item.getCostItem().getType());
/*  23 */     lore.add(ChatColor.GREEN + "Właściciel: " + ChatColor.GRAY + item.getOwner());
/*  24 */     lore.add(ChatColor.GREEN + "Item Sell ID: " + ChatColor.GRAY + currentitem);
/*  25 */     lore.add(ChatColor.GREEN + "Pozostały czas: " + ChatColor.GRAY + item.time + " minuty");
/*  26 */     if (item.lore != null) {
/*  27 */       for (int i = 0; i < item.lore.size(); i++) {
/*  28 */         lore.add(item.lore.get(i));
      }
    }

/*  32 */     return lore;
  }

  private static ItemStack createNextPageArrow() {
/*  36 */     ItemStack nextPage = new ItemStack(Material.ARROW);
/*  37 */     ItemMeta itemMeta = nextPage.getItemMeta();
/*  38 */     itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Następna Strona");
/*  39 */     nextPage.setItemMeta(itemMeta);
/*  40 */     return nextPage;
  }

  private static ItemStack createPrevPageArrow() {
/*  44 */     ItemStack prevPage = new ItemStack(Material.ARROW);
/*  45 */     ItemMeta itemMeta = prevPage.getItemMeta();
/*  46 */     itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Poprzednia Strona");
/*  47 */     prevPage.setItemMeta(itemMeta);
/*  48 */     return prevPage;
  }
/*  50 */   public static List<String> marketOpenPlayerList = new ArrayList<>();
/*  51 */   public static List<Inventory> marketPage = new ArrayList<>();
/*  52 */   public static HashMap<String, Integer> currentPage = new HashMap<>();

  public static void openMarket(Player player) {
/*  55 */     if (marketPage != null) {
/*  56 */       marketPage.clear();
    }
/*  58 */     Inventory bufor = Bukkit.createInventory(null, 54, "Market");
/*  59 */     int current_item = 1;
/*  60 */     int max_item = 1;
/*  61 */     int page = 0;
/*  62 */     for (MarketItemClass.MarketItem item : MarketItemClass.marketItems) {

/*  64 */       if (current_item < 45) {

/*  66 */         ItemStack itemStack3 = item.getSellingItem();
/*  67 */         ItemMeta itemMeta = itemStack3.getItemMeta();
/*  68 */         List<String> lore = new ArrayList<>();
/*  69 */         itemMeta.setLore(createLore(item, max_item));
/*  70 */         itemStack3.setItemMeta(itemMeta);
/*  71 */         bufor.addItem(new ItemStack[] { itemStack3 });
/*  72 */         current_item++;
/*  73 */         max_item++;

        continue;
      }

/*  78 */       ItemStack itemStack1 = createNextPageArrow();
/*  79 */       ItemStack itemStack2 = createPrevPageArrow();
/*  80 */       bufor.setItem(53, itemStack1);
/*  81 */       bufor.setItem(45, itemStack2);
/*  82 */       Inventory inventory = Bukkit.createInventory(null, 54, "Strona " + page);
/*  83 */       inventory.setContents(bufor.getContents());
/*  84 */       marketPage.add(page, inventory);
/*  85 */       bufor.clear();
/*  86 */       current_item = 1;
/*  87 */       page++;
/*  88 */       ItemStack itemStack = item.getSellingItem();
/*  89 */       ItemMeta meta = itemStack.getItemMeta();
/*  90 */       meta.setLore(createLore(item, max_item));
/*  91 */       itemStack.setItemMeta(meta);
/*  92 */       bufor.addItem(new ItemStack[] { itemStack });
/*  93 */       current_item++;
/*  94 */       max_item++;
    }

/*  97 */     ItemStack nextPage = createNextPageArrow();
/*  98 */     ItemStack prevPage = createPrevPageArrow();
/*  99 */     bufor.setItem(53, nextPage);
/* 100 */     bufor.setItem(45, prevPage);
/* 101 */     Inventory bufor2 = Bukkit.createInventory(null, 54, "Strona " + page);
/* 102 */     bufor2.setContents(bufor.getContents());
/* 103 */     marketPage.add(page, bufor2);
/* 104 */     player.openInventory(marketPage.get(0));
/* 105 */     if (marketOpenPlayerList.contains(player.getName()))
    {

/* 108 */       marketOpenPlayerList.remove(player.getName());
    }
/* 110 */     marketOpenPlayerList.add(player.getName());
/* 111 */     if (currentPage.containsKey(player.getName()))
    {
/* 113 */       currentPage.remove(player.getName());
    }
/* 115 */     currentPage.put(player.getName(), Integer.valueOf(0));
  }

  public static void openNextPage(Player player) {
/* 119 */     int player_page = ((Integer)currentPage.get(player.getName())).intValue();
/* 120 */     int max_page = MarketItemClass.marketItems.size() / 45;
/* 121 */     if (player_page < max_page) {

/* 123 */       player.closeInventory();
/* 124 */       player.openInventory(marketPage.get(player_page + 1));
/* 125 */       currentPage.remove(player.getName());
/* 126 */       currentPage.put(player.getName(), Integer.valueOf(player_page + 1));
/* 127 */       marketOpenPlayerList.add(player.getName());
    }
  }

  public static void openPrevPage(Player player) {
/* 132 */     int player_page = ((Integer)currentPage.get(player.getName())).intValue();
/* 133 */     if (player_page > 0) {

/* 135 */       player.closeInventory();
/* 136 */       player.openInventory(marketPage.get(player_page - 1));
/* 137 */       currentPage.remove(player.getName());
/* 138 */       currentPage.put(player.getName(), Integer.valueOf(player_page - 1));
/* 139 */       marketOpenPlayerList.add(player.getName());
    }
  }

  public static void updateInventory() {
/* 144 */     for (int i = 0; i < marketOpenPlayerList.size(); i++) {

/* 146 */       for (Player player : Bukkit.getOnlinePlayers()) {

/* 148 */         if (player.getName().equalsIgnoreCase(marketOpenPlayerList.get(i))) {

/* 150 */           player.closeInventory();
/* 151 */           openMarket(player);
        }
      }
    }
  }
}