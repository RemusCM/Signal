package org.thoughtcrime.securesms;

public enum PermissionType {
  EDIT_GROUP(64),
  CLEAR_GROUP_CONVERSATION(32);


  private final int permissionCode;

  PermissionType(int permissionCode) {
    this.permissionCode = permissionCode;
  }

  public String getPermissionTypeCode() {
    return String.valueOf(this.permissionCode);
  }

}