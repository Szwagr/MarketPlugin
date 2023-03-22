 package me.szwagru.market;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import me.szwagru.market.lang.lang;
/*     */ import me.szwagru.market.settings.settings;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.configuration.ConfigurationSection;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryCloseEvent;
/*     */ import org.bukkit.event.player.PlayerJoinEvent;
/*     */ import org.bukkit.event.player.PlayerQuitEvent;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import org.bukkit.scheduler.BukkitScheduler;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TradeMarket
/*     */   extends JavaPlugin
/*     */   implements Listener
/*     */ {
/*     */   public String col(String message) {
/*  42 */     return ChatColor.translateAlternateColorCodes('&', message);
/*     */   }
/*  44 */   private Map<String, List<ItemStack>> receiveItems = new HashMap<>();
/*  45 */   private List<String> ReceiveOpen = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public void saveMarketItemsToFile() {
/*     */     try {
/*  50 */       File itemsFile = new File(getDataFolder(), "market.yml");
/*  51 */       YamlConfiguration config = YamlConfiguration.loadConfiguration(itemsFile);
/*  52 */       config.set("items", null);
/*  53 */       for (int i = 0; i < MarketItemClass.marketItems.size(); i++) {
/*  54 */         MarketItemClass.MarketItem item = MarketItemClass.marketItems.get(i);
/*  55 */         String itemKey = "items." + i;
/*  56 */         config.set(itemKey + ".sellingItem", item.getSellingItem());
/*  57 */         config.set(itemKey + ".costItem", item.getCostItem());
/*  58 */         config.set(itemKey + ".owner", item.getOwner());
                  config.set(itemKey + ".remaining", item.time);
/*  59 */         List<String> lore = new ArrayList<>();
/*  60 */         if (item.lore != null)
/*     */         {
/*  62 */           lore = item.lore;
/*     */         }
/*     */         
/*  65 */         if (lore != null && !lore.isEmpty()) {
/*  66 */           config.set(itemKey + ".lore", lore);
/*     */         }
/*     */       } 
/*  69 */       config.save(itemsFile);
/*  70 */     } catch (IOException e) {
/*  71 */       getLogger().severe("Error saving items.yml: " + e.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public <Int> List<MarketItemClass.MarketItem> loadMarketItemsFromFile() {
/*  76 */     List<MarketItemClass.MarketItem> marketItems = new ArrayList<>();
/*     */     
/*  78 */     File itemsFile = new File(getDataFolder(), "market.yml");
/*  79 */     if (!itemsFile.exists()) {
/*  80 */       return marketItems;
/*     */     }
/*     */     
/*  83 */     YamlConfiguration config = YamlConfiguration.loadConfiguration(itemsFile);
/*  84 */     ConfigurationSection itemsSection = config.getConfigurationSection("items");
/*  85 */     if (itemsSection == null) {
/*  86 */       return marketItems;
/*     */     }
/*     */     
/*  89 */     for (String key : itemsSection.getKeys(false)) {
/*  90 */       ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
/*  91 */       ItemStack sellingItem = itemSection.getItemStack("sellingItem");
/*  92 */       ItemStack costItem = itemSection.getItemStack("costItem");
/*  93 */       String owner = itemSection.getString("owner");
                //int time = itemSection.getInt("remaining");
/*  94 */       List<String> lore = new ArrayList<>();
/*  95 */       if (itemSection.getStringList("lore") != null)
/*  96 */         lore = itemSection.getStringList("lore"); 
/*  97 */       MarketItemClass.MarketItem marketItem = new MarketItemClass.MarketItem(sellingItem, costItem, owner);
/*  98 */       if (lore != null)
/*     */       {
/* 100 */         marketItem.lore = lore;
/*     */       }
                marketItem.time = itemSection.getInt("remaining");
/* 103 */       marketItems.add(marketItem);
/*     */     } 
/*     */     
/* 106 */     return marketItems;
/*     */   }
/*     */   
/*     */   private void saveReceiveItems() {
/* 110 */     File file = new File(getDataFolder(), "receive.yml");
/* 111 */     YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
/* 112 */     for (Map.Entry<String, List<ItemStack>> entry : this.receiveItems.entrySet()) {
/* 113 */       config.set(entry.getKey(), entry.getValue());
/*     */     }
/*     */     try {
/* 116 */       config.save(file);
/* 117 */     } catch (IOException e) {
/* 118 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   private void loadIReceiveItems() {
/* 122 */     File file = new File(getDataFolder(), "receive.yml");
/* 123 */     YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
/* 124 */     for (String key : yamlConfiguration.getKeys(false)) {
/* 125 */       List<ItemStack> value = (List<ItemStack>) yamlConfiguration.getList(key);
/* 126 */       this.receiveItems.put(key, value);
/*     */     } 
/*     */   }
/*     */   private void addItemToOwner(String owner, ItemStack item) {
/* 130 */     List<ItemStack> playerItems = this.receiveItems.get(owner);
/* 131 */     if (playerItems == null) {
/* 132 */       playerItems = new ArrayList<>();
/*     */     }
/* 134 */     ItemMeta meta = item.getItemMeta();
/* 135 */     List<String> lore = new ArrayList<>();
/* 136 */     lore.add("Item ID: " + playerItems.size());
/* 137 */     if (item.getItemMeta().getLore() != null) {
/* 138 */       for (int i = 0; i < item.getItemMeta().getLore().size(); i++) {
/* 139 */         lore.add(item.getItemMeta().getLore().get(i));
/*     */       }
/*     */     }
/* 142 */     meta.setLore(lore);
/* 143 */     item.setItemMeta(meta);
/* 144 */     playerItems.add(item);
/* 145 */     this.receiveItems.put(owner, playerItems);
/* 146 */     saveReceiveItems();
/*     */   }
/*     */   
/*     */   private void decreaseTime(List<MarketItemClass.MarketItem> exampleList) {
/* 150 */     List<MarketItemClass.MarketItem> toRemove = new ArrayList<>();
/* 151 */     for (int i = 0; i < exampleList.size(); i++) {
/*     */       
/* 153 */       ((MarketItemClass.MarketItem)exampleList.get(i)).time--;
/* 154 */       if (((MarketItemClass.MarketItem)exampleList.get(i)).time <= 1) {
/*     */         
/* 156 */         String owner = ((MarketItemClass.MarketItem)exampleList.get(i)).getOwner();
/* 157 */         ItemStack itemStack = ((MarketItemClass.MarketItem)exampleList.get(i)).getSellingItem();
/* 158 */         ItemMeta meta = itemStack.getItemMeta();
/*     */ 
/*     */         
/* 161 */         List<String> lore = meta.getLore();
/* 162 */         lore.clear();
/* 163 */         if (((MarketItemClass.MarketItem)exampleList.get(i)).lore != null)
/* 164 */           lore = ((MarketItemClass.MarketItem)exampleList.get(i)).lore; 
/* 165 */         meta.setLore(lore);
/* 166 */         itemStack.setItemMeta(meta);
/* 167 */         addItemToOwner(owner, itemStack);
/* 168 */         toRemove.add(exampleList.get(i));
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 174 */         ItemStack itemStack = ((MarketItemClass.MarketItem)exampleList.get(i)).getSellingItem();
/* 175 */         ItemMeta meta = itemStack.getItemMeta();
/* 176 */         List<String> old_meta = new ArrayList<>();
/* 177 */         old_meta = meta.getLore();
/* 178 */         if (old_meta != null && 
/* 179 */           old_meta.size() >= 3) {
/* 180 */           if (old_meta != null)
/* 181 */             old_meta.remove(3); 
/* 182 */           if (old_meta != null) {
/* 183 */             old_meta.add(3, ChatColor.GREEN + "Time remaining: " + ChatColor.GRAY + ((MarketItemClass.MarketItem)exampleList.get(i)).time);
/*     */           }
/*     */         } 
/* 186 */         MarketItemClass.MarketItem bufor = exampleList.get(i);
/* 187 */         bufor.getSellingItem().getItemMeta().setLore(old_meta);
/* 188 */         exampleList.remove(i);
/* 189 */         exampleList.add(i, bufor);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 194 */     MarketInventory.updateInventory();
/* 195 */     exampleList.removeAll(toRemove);
/* 196 */     saveMarketItemsToFile();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/* 201 */     settings.setup();
/* 202 */     settings.defaultsetup();
/* 203 */     settings.get().options().copyDefaults(true);
/* 204 */     settings.save();
/* 205 */     MarketItemClass.marketItems = loadMarketItemsFromFile();
/* 206 */     Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)this);
/* 207 */     loadIReceiveItems();
/* 208 */     BukkitScheduler scheduler = getServer().getScheduler();
/* 209 */     scheduler.scheduleSyncRepeatingTask((Plugin)this, new Runnable()
/*     */         {
/*     */           public void run() {
/* 212 */             TradeMarket.this.decreaseTime(MarketItemClass.marketItems);
/*     */           }
/*     */         },  0L, 1200L);
/* 215 */     lang.setup();
/* 216 */     lang.defaultsetup();
/* 217 */     lang.get().options().copyDefaults(true);
/* 218 */     lang.save();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
/* 223 */     if (command.getName().equalsIgnoreCase("trademarket")) {
/*     */       
/* 225 */       if (!(sender instanceof Player)) {
/* 226 */         sender.sendMessage("This command can only be run by a player.");
/* 227 */         return false;
/*     */       } 
/* 229 */       if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
/*     */         
/* 231 */         Player player = (Player)sender;
/* 232 */         player.sendMessage(col(lang.get().getString("help-1")));
/* 233 */         player.sendMessage(col(lang.get().getString("help-2")));
/* 234 */         player.sendMessage(col(lang.get().getString("help-3")));
/* 235 */         player.sendMessage(col(lang.get().getString("help-4")));
/*     */       } 
/* 237 */       if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
/*     */         
/* 239 */         Player player = (Player)sender;
/* 240 */         player.sendMessage(ChatColor.GREEN + "Author: Gujix64 && Szwagru");
/* 241 */         player.sendMessage(ChatColor.GREEN + "Version: 1.0 - Beta");
/*     */       } 
/*     */       
/* 245 */       if (args.length == 3 && args[0].equalsIgnoreCase("sell")) {
/*     */         
/* 247 */         Player player = (Player)sender;
/* 248 */         ItemStack itemToSell = player.getInventory().getItemInMainHand();
/* 249 */         String costItemID = args[1];
/* 250 */         if (!Operations.ValidateNumber(args[2]))
/*     */         {
/* 252 */           return false;
/*     */         }
/* 254 */         int costItemValue = Integer.parseInt(args[2]);
/* 255 */         if (itemToSell.getType() == Material.AIR) {
/* 256 */           player.sendMessage(col(lang.get().getString("error-1")));
/* 257 */           return false;
/*     */         } 
/* 259 */         if (!Operations.checkMaterial(args[1], player))
/*     */         {
/* 261 */           return false;
/*     */         }
/* 263 */         int number = 0;
/* 264 */         for (int i = 0; i < MarketItemClass.marketItems.size(); i++) {
/*     */           
/* 266 */           if (((MarketItemClass.MarketItem)MarketItemClass.marketItems.get(i)).getOwner().equalsIgnoreCase(player.getName()))
/*     */           {
/* 268 */             number++;
/*     */           }
/*     */         } 
/* 271 */         if (number >= settings.get().getInt("max_items")) {
/*     */           
/* 273 */           player.sendMessage(col(lang.get().getString("error-2")));
/* 274 */           return false;
/*     */         } 
/* 276 */         if (costItemValue >= settings.get().getInt("max_amount_of_item_sell")) {
/*     */           
/* 278 */           player.sendMessage(col(lang.get().getString("error-6")));
/* 279 */           return false;
/*     */         }
                  if (costItemValue <= 0){
                      player.sendMessage(col(lang.get().getString("error-7")));
                      return false;
                  }
/* 281 */         Material material = Material.matchMaterial(args[1]);
/* 282 */         ItemStack costItemStack = new ItemStack(material, costItemValue);
/* 283 */         MarketItemClass.addMarketItem(itemToSell, costItemStack, player.getName());
/* 284 */         saveMarketItemsToFile();
/* 285 */         player.sendMessage(ChatColor.GRAY + "" + itemToSell.getAmount() + "x " + itemToSell.getType() + " " + col(lang.get().getString("message-1")) + " " + costItemValue + "x " + costItemID + ".");
/* 286 */         player.getInventory().setItemInMainHand(null);
/* 287 */         return true;
/*     */       } 
/* 289 */       if (args.length == 0) {
/*     */         
/* 291 */         Player player = (Player)sender;
/* 292 */         MarketInventory.openMarket(player);
/*     */       }
/* 294 */       else if (args.length == 1 && args[0].equalsIgnoreCase("receive")) {
/*     */         
/* 296 */         Player player = (Player)sender;
/* 297 */         String owner = player.getName();
/* 298 */         if (!this.receiveItems.containsKey(owner) || ((List)this.receiveItems.get(owner)).isEmpty()) {
/* 299 */           player.sendMessage(col(lang.get().getString("error-3")));
/* 300 */           return true;
/*     */         } 
/* 302 */         List<ItemStack> playerItems = this.receiveItems.get(owner);
/* 303 */         Inventory inventory = Bukkit.createInventory(null, 27, "Receive Items");
/* 304 */         for (ItemStack item : playerItems) {
/* 305 */           inventory.addItem(new ItemStack[] { item });
/*     */         } 
/*     */         
/* 308 */         player.openInventory(inventory);
/* 309 */         this.ReceiveOpen.add(player.getName());
/* 310 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 314 */     return false;
/*     */   }
/*     */   
/*     */   public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
/* 318 */     List<String> tabCompletions = new ArrayList<>();
/* 319 */     if (args.length == 1) {
/* 320 */       tabCompletions.add("help");
/* 321 */       tabCompletions.add("info");
/* 322 */       tabCompletions.add("sell");
/* 323 */       tabCompletions.add("receive");
/* 324 */     } else if (args.length == 2 && args[0].equalsIgnoreCase("sell")) {
/* 325 */       tabCompletions.addAll(Arrays.asList(new String[] {"diamond"}));
/*     */     } 
/* 327 */     return tabCompletions;
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onInventoryClick(InventoryClickEvent event) {
/* 333 */     Player player = (Player)event.getWhoClicked();
/* 334 */     if (MarketInventory.marketOpenPlayerList.contains(player.getName())) {
/*     */ 
/*     */       
/* 337 */       event.setCancelled(true);
/* 338 */       if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
/*     */         return;
/*     */       }
/* 341 */       if (event.getCurrentItem().getType() == Material.ARROW && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Następna Strona")) {
/*     */         
/* 343 */         MarketInventory.openNextPage(player);
/*     */         return;
/*     */       } 
/* 346 */       if (event.getCurrentItem().getType() == Material.ARROW && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Poprzednia Strona")) {
/*     */         
/* 348 */         MarketInventory.openPrevPage(player);
/*     */         return;
/*     */       } 
/* 351 */       for (MarketItemClass.MarketItem item : MarketItemClass.marketItems) {
/*     */

/* 353 */         if (item.getSellingItem().isSimilar(event.getCurrentItem())) {

                    if (item.getOwner().equalsIgnoreCase(player.getName())) {
                        if(event.isShiftClick()){
                            player.getInventory().removeItem(new ItemStack[] { item.getCostItem() });
                            ItemMeta itemMeta = item.getSellingItem().getItemMeta();
                            String key = itemMeta.getLore().get(1);
                            String value = itemMeta.getLore().get(0);
                            List<String> lore = itemMeta.getLore();
                            List<String> old_lore = new ArrayList<>();
                            for (int i = 4; i < lore.size(); i++)
                            {
                                old_lore.add(lore.get(i));
                            }

                            itemMeta.setLore(old_lore);
                            item.getSellingItem().setItemMeta(itemMeta);
                            player.getInventory().addItem(new ItemStack[] { item.getSellingItem() });
                            event.getClickedInventory().removeItem(new ItemStack[] { event.getCurrentItem() });
                            MarketItemClass.marketItems.remove(item);
                            saveMarketItemsToFile();
                            player.sendMessage(col(lang.get().getString("message-7")));
                            return;
                        }else{
                            player.sendMessage(col(lang.get().getString("error-4")));
                            return;
                        }
                    }

/* 355 */           if (player.getInventory().containsAtLeast(item.getCostItem(), item.getCostItem().getAmount())) {
/*     */

/* 362 */             player.getInventory().removeItem(new ItemStack[] { item.getCostItem() });
/* 363 */             ItemMeta itemMeta = item.getSellingItem().getItemMeta();
/* 364 */             String key = itemMeta.getLore().get(1);
/* 365 */             String value = itemMeta.getLore().get(0);
/* 366 */             List<String> lore = itemMeta.getLore();
/* 367 */             List<String> old_lore = new ArrayList<>();
/* 368 */             for (int i = 4; i < lore.size(); i++)
/*     */             {
/* 370 */               old_lore.add(lore.get(i));
/*     */             }
/*     */             
/* 373 */             itemMeta.setLore(old_lore);
/* 374 */             item.getSellingItem().setItemMeta(itemMeta);
/* 375 */             player.getInventory().addItem(new ItemStack[] { item.getSellingItem() });
/* 376 */             event.getClickedInventory().removeItem(new ItemStack[] { event.getCurrentItem() });
/* 377 */             MarketItemClass.marketItems.remove(item);
/* 378 */             saveMarketItemsToFile();
/* 379 */             player.sendMessage(col(lang.get().getString("message-2")) + item.getSellingItem().getAmount() + "x " + item.getSellingItem().getType() + " " + col(lang.get().getString("message-3")) + " " + item.getCostItem().getAmount() + "x " + item.getCostItem().getType());
/* 380 */             MarketInventory.updateInventory();
/* 381 */             String owner = key.substring(11);
/* 382 */             Pattern pattern = Pattern.compile("7(\\d+)x");
/* 383 */             Matcher matcher = pattern.matcher(value);
/* 384 */             int price = 0;
/* 385 */             if (matcher.find()) {
/* 386 */               price = Integer.parseInt(matcher.group(1));
/*     */             }
/*     */             
/* 389 */             String item_id = value.split(" ")[2];
/* 390 */             Material material = Material.matchMaterial(item_id);
/* 391 */             ItemStack itemStack1 = new ItemStack(material, price);
/* 392 */             addItemToOwner(owner, itemStack1);
/* 393 */             Player ownerPlayer = Bukkit.getPlayer(owner);
/* 394 */             if (ownerPlayer != null)
/*     */             {
/* 396 */               ownerPlayer.sendMessage(col(lang.get().getString("message-4")));
/*     */             }
/*     */             break;
/*     */           } 
/* 400 */           player.sendMessage(col(lang.get().getString("error-5")) + item.getCostItem().getType() + " " + col(lang.get().getString("message-5")) + " " + item.getSellingItem().getAmount() + "x " + item.getSellingItem().getType());
/*     */ 
/*     */           
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 409 */     Inventory clickedInventory = event.getClickedInventory();
/* 410 */     if (this.ReceiveOpen.contains(event.getWhoClicked().getName()) && 
/* 411 */       clickedInventory != null && clickedInventory.getHolder() instanceof Player) {
/* 412 */       event.setCancelled(true);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 417 */     Inventory inventory = event.getInventory();
/* 418 */     if (inventory == null || !event.getView().getTitle().equals("Receive Items")) {
/*     */       return;
/*     */     }
/* 421 */     if (inventory != null && event.getView().getTitle().equalsIgnoreCase("Receive Items")) {
/*     */       
/* 423 */       event.setCancelled(true);
/* 424 */       ItemStack item = event.getCurrentItem();
/* 425 */       if (item != null && item.getType() != Material.AIR) {
/* 426 */         String owner = player.getName();
/* 427 */         List<ItemStack> playerItems = this.receiveItems.get(owner);
/* 428 */         playerItems.remove(item);
/* 429 */         this.receiveItems.remove(owner);
/* 430 */         this.receiveItems.put(owner, playerItems);
/* 431 */         saveReceiveItems();
/* 432 */         ItemMeta meta = item.getItemMeta();
/* 433 */         List<String> lore = meta.getLore();
/* 434 */         if (lore != null) {
/* 435 */           lore.remove(0);
/*     */         }
/* 437 */         meta.setLore(lore);
/* 438 */         item.setItemMeta(meta);
/* 439 */         inventory.remove(item);
/* 440 */         player.getInventory().addItem(new ItemStack[] { item });
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void OnPlayerQuitEvent(PlayerQuitEvent event) {
/* 453 */     MarketInventory.marketOpenPlayerList.remove(event.getPlayer().getName());
/* 454 */     this.ReceiveOpen.remove(event.getPlayer().getName());
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void OnPlayerJoinEvent(PlayerJoinEvent event) {
/* 459 */     MarketInventory.marketOpenPlayerList.remove(event.getPlayer().getName());
/* 460 */     this.ReceiveOpen.remove(event.getPlayer().getName());
/* 461 */     if (this.receiveItems.containsKey(event.getPlayer().getName())) {
/*     */       
/* 463 */       event.getPlayer().sendMessage("");
/* 464 */       event.getPlayer().sendMessage("");
/* 465 */       event.getPlayer().sendMessage(col(lang.get().getString("message-6")));
/* 466 */       event.getPlayer().sendMessage("");
/* 467 */       event.getPlayer().sendMessage("");
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onInventoryClose(InventoryCloseEvent event) {
/* 473 */     Player player = (Player)event.getPlayer();
/* 474 */     if (MarketInventory.marketOpenPlayerList.contains(player.getName()))
/*     */     {
/* 476 */       MarketInventory.marketOpenPlayerList.remove(player.getName());
/*     */     }
/* 478 */     if (this.ReceiveOpen.contains(player.getName()))
/*     */     {
/* 480 */       this.ReceiveOpen.remove(event.getPlayer().getName());
/*     */     }
/*     */   }
/*     */ }