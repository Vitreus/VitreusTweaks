package online.vitreusmc.vitreusTweaks.db.object;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;

import net.md_5.bungee.api.ChatColor;

@Entity(value = "Players", noClassnameStored = true)
public class VitreusPlayer {

	@Id
	private int id;
	
	@Indexed(options = @IndexOptions(unique = true))
	private String UUID;
	
	private String username;
	private ChatColor color;
	
	private Date lastOnline;
	private int minutesPlayed;
	
	public enum PlayerFlag {
		WHITELISTED,
		BANNED,
		TWELVE_HOUR_MILESTONE,
		TWENTY_FOUR_HOUR_MILESTONE,
		PATREON
	};
	@Embedded
	private Set<PlayerFlag> flags = new HashSet();
	public void setUUID(String UUID) {
		this.UUID = UUID;
	}
	
	public String getUUID() {
		return UUID;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setColor(ChatColor color) {
		this.color = color;
	}
	
	public ChatColor getColor() {
		return color;
	}
	
	public void setLastOnline(Date date) {
		this.lastOnline = date;
	}
	
	public Date getLastOnline() {
		return lastOnline;
	}
	
	public void setMinutesPlayed(int minutesPlayed) {
		this.minutesPlayed = minutesPlayed;
	}
	
	public int getMinutesPlayed() {
		return minutesPlayed;
	}
	
	public void addFlag(PlayerFlag flag) {
		flags.add(flag);
	}
	
	public void removeFlag(PlayerFlag flag) {
		flags.remove(flag);
	}
	
	public Set<PlayerFlag> getFlags() {
		return flags;
	}
	
	public boolean hasFlag(PlayerFlag flag) {
		return flags.contains(flag);
	}
	
}
