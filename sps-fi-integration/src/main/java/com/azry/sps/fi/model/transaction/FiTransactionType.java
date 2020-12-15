package com.azry.sps.fi.model.transaction;

import com.azry.sps.common.model.transaction.TransactionType;

public enum FiTransactionType {
	PRINCIPAL,
	COMMISSION;

	public static FiTransactionType toFiTransactionType(TransactionType type) {
		if (type == TransactionType.CLIENT_COMMISSION_AMOUNT) {
			return COMMISSION;
		} else {
			return PRINCIPAL;
		}
	}
}