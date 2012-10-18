package com.craftyn.casinoslots.util;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyUniverse;

public class TownyChecks {
	protected CasinoSlots plugin;
	
	public TownyChecks(CasinoSlots plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Checks if the player is part of a town, if towny checking is enabled.
	 * 
	 * <p>
	 * 
	 * This will always return true if the player is a CasinoSlots admin.
	 * 
	 * @param player The player to check.
	 * @return True if the player is part of a town (or admin), false if not.
	 */
	public boolean checkTown(Player player) {
		if(plugin.permission.isAdmin(player)) return true;
		Resident res = null;
		
		try {
			res = TownyUniverse.getDataSource().getResident(player.getName());
		} catch (NotRegisteredException e) {return false;}
		
		return res.hasTown();
	}
	
	/**
	 * Checks if the player is a mayor, if towny checking is enabled.
	 * 
	 * <p>
	 * 
	 * This will always return true if the player is a CasinoSlots admin.
	 * 
	 * @param player The player to check.
	 * @return True if the player is a king (or admin), false if not.
	 */
	public boolean checkMayor(Player player) {
		if(plugin.permission.isAdmin(player)) return true;
		Resident res = null;
		
		try {
			res = TownyUniverse.getDataSource().getResident(player.getName());
		} catch (NotRegisteredException e) {return false;}
		
		return res.isMayor();
	}

	/**
	 * Since the Towny check was enabled, we need to check if the player is the owner of the block.
	 * 
	 * @param check The soon to be controller block to check.
	 * @param player The name of the player to check, normal case.
	 * @return True if the player can "build", false if not.
	 */
	public boolean checkSingleTowny(Block check, String player) {
		Resident res = null, resC = null;
		TownBlock tbC = TownyUniverse.getTownBlock(check.getLocation());
		
		if(tbC == null)
			return false;
		
		try {
			res = TownyUniverse.getDataSource().getResident(player);
			resC = tbC.getResident();
		} catch (NotRegisteredException e) {return false;}
		
		if(plugin.configData.inDebug()) plugin.debug("The single block we're check resident is: " + resC.getName());
		
		if(res.equals(resC))
			return true;
		else
			return false;
	}

	/**
	 * Since the Towny check was enabled, we need to check if the player is the owner of the block and the other two.
	 * 
	 * @param check The center block to check.
	 * @param face 
	 * @param player The name of the player to check, normal case.
	 * @return True if the player can "build", false if not.
	 */
	public boolean checkSlotsTowny(Block check, BlockFace face, String player) {
		Resident res = null, resL = null, resC = null, resR = null;
		TownBlock tbL = TownyUniverse.getTownBlock(check.getRelative(getDirection(face, "left"), 2).getLocation());
		TownBlock tbC = TownyUniverse.getTownBlock(check.getLocation());
		TownBlock tbR = TownyUniverse.getTownBlock(check.getRelative(getDirection(face, "left"), 2).getLocation());
		
		if(tbL == null || tbC == null || tbR == null)
			return false;
		
		try {
			res = TownyUniverse.getDataSource().getResident(player);
			resL = tbL.getResident();
			resC = tbC.getResident();
			resR = tbR.getResident();
		} catch (NotRegisteredException e) {return false;}
		
		if(plugin.configData.inDebug()) plugin.debug("The left block resident is: " + resL.getName());
		if(plugin.configData.inDebug()) plugin.debug("The center block resident is: " + resC.getName());
		if(plugin.configData.inDebug()) plugin.debug("The right block resident is: " + resR.getName());
		
		if(res.equals(resL) && res.equals(resC) && res.equals(resR))
			return true;
		else
			return false;
	}
	
	// Used for orienting the slot machine correctly
	private BlockFace getDirection(BlockFace face, String direction) {
		if(face == BlockFace.NORTH) {
			if(direction.equalsIgnoreCase("left")) {
				return BlockFace.EAST;
			} else if(direction.equalsIgnoreCase("right")) {
				return BlockFace.WEST;
			}
		} else if(face == BlockFace.SOUTH) {
			if(direction.equalsIgnoreCase("left")) {
				return BlockFace.WEST;
			} else if(direction.equalsIgnoreCase("right")) {
				return BlockFace.EAST;
			}
		} else if(face == BlockFace.WEST) {
			if(direction.equalsIgnoreCase("left")) {
				return BlockFace.SOUTH;
			} else if(direction.equalsIgnoreCase("right")) {
				return BlockFace.NORTH;
			}
		} else if(face == BlockFace.EAST) {
			if(direction.equalsIgnoreCase("left")) {
				return BlockFace.NORTH;
			} else if(direction.equalsIgnoreCase("right")) {
				return BlockFace.SOUTH;
			}
		}
		
		return BlockFace.SELF;
	}

}
