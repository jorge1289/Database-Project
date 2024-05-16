package edu.berkeley.cs186.database.concurrency;

/**
 * Utility methods to track the relationships between different lock types.
 */
public enum LockType {
    S,   // shared
    X,   // exclusive
    IS,  // intention shared
    IX,  // intention exclusive
    SIX, // shared intention exclusive
    NL;  // no lock held

    /**
     * This method checks whether lock types A and B are compatible with
     * each other. If a transaction can hold lock type A on a resource
     * at the same time another transaction holds lock type B on the same
     * resource, the lock types are compatible.
     */
    public static boolean compatible(LockType a, LockType b) {
        if (a == null || b == null) {
            throw new NullPointerException("null lock type");
        }
        // TODO(proj4_part1): implement
        if (a.equals(NL) || b.equals(NL)) {
            return true;
        } else if (a.equals(X) || b.equals(X)) {
            return false;
        } else if (a.equals(IX)) {
            return b.equals(IX) || b.equals(IS);
        } else if (a.equals(S)) {
            return b.equals(S) || b.equals(IS);
        } else if (a.equals(SIX)) {
            return b.equals(IS);
        } else {
            return true;
        }
    }

    /**
     * This method returns the lock on the parent resource
     * that should be requested for a lock of type A to be granted.
     */
    public static LockType parentLock(LockType a) {
        if (a == null) {
            throw new NullPointerException("null lock type");
        }
        switch (a) {
        case S: return IS;
        case X: return IX;
        case IS: return IS;
        case IX: return IX;
        case SIX: return IX;
        case NL: return NL;
        default: throw new UnsupportedOperationException("bad lock type");
        }
    }

    /**
     * This method returns if parentLockType has permissions to grant a childLockType
     * on a child.
     */
    public static boolean canBeParentLock(LockType parentLockType, LockType childLockType) {
        if (parentLockType == null || childLockType == null) {
            throw new NullPointerException("null lock type");
        }
        // TODO(proj4_part1): implement
        // TODO: COMEBACK TO FOR PART 2
        switch (parentLockType) {
            case S: return false;
            case X: return false;
            case IS: return childLockType == S || childLockType == IS;
            case IX: return true;
            case SIX: return childLockType == IX || childLockType == X;
            case NL: return false;
            default: throw new UnsupportedOperationException("bad lock type");
        }
    }

    /**
     * This method returns whether a lock can be used for a situation
     * requiring another lock (e.g. an S lock can be substituted with
     * an X lock, because an X lock allows the transaction to do everything
     * the S lock allowed it to do).
     */
    public static boolean substitutable(LockType substitute, LockType required) {
        if (required == null || substitute == null) {
            throw new NullPointerException("null lock type");
        }
        // TODO(proj4_part1): implement
        // TODO: COMEBACK TO FOR PART 2
        if (substitute.equals(NL)) {
            return required.equals(NL);
        } else if (required.equals(X)) {
            return substitute.equals(X);
        } else if (required.equals(S)) {
            return substitute.equals(S) || substitute.equals(SIX) || substitute.equals(X);
        } else if (required.equals(IX)) {
            return substitute.equals(IX) || substitute.equals(X) || substitute.equals(SIX) || substitute.equals(S);
        } else if (required.equals(IS)) {
            return substitute.equals(IX) || substitute.equals(IS) || substitute.equals(X) || substitute.equals(SIX) || substitute.equals(S);
        }   else {
            return true;
        }
    }

    /**
     * @return True if this lock is IX, IS, or SIX. False otherwise.
     */
    public boolean isIntent() {
        return this == LockType.IX || this == LockType.IS || this == LockType.SIX;
    }

    @Override
    public String toString() {
        switch (this) {
        case S: return "S";
        case X: return "X";
        case IS: return "IS";
        case IX: return "IX";
        case SIX: return "SIX";
        case NL: return "NL";
        default: throw new UnsupportedOperationException("bad lock type");
        }
    }
}

