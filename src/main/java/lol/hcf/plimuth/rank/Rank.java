package lol.hcf.plimuth.rank;

import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;

public class Rank implements Permissible {

    private final String id;

    private String prefix;
    private Set<Rank> parents;

    private Set<String> permissions;

    public Rank(String id) {
        this.id = id;
        this.permissions = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public String getPrefix() {
        return prefix;
    }

    public Rank setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public Set<Rank> getParents() {
        return parents;
    }

    public Rank setParents(Set<Rank> parents) {
        this.parents = parents;
        return this;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public Rank setPermissions(Set<String> permissions) {
        this.permissions = permissions;
        return this;
    }

    @Override
    public boolean isPermissionSet(String name) {
        return true;
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return true;
    }

    @Override
    public boolean hasPermission(String name) {
        return this.permissions.contains(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return this.permissions.contains(perm.getName());
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {}

    @Override
    public void recalculatePermissions() {}

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOp() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException();
    }
}
