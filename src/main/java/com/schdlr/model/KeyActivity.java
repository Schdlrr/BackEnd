package com.schdlr.model;

/*
 * Enum representing the activity status of a key.
 * 
 * Values:
 * - `ACTIVE`: The key is active and valid for use.
 * - `GRACE`: The key is in a grace period, it can only be used to validate already existing
 * refresh tokens but not make new ones.
 */

public enum KeyActivity {
    ACTIVE,
    GRACE

}
