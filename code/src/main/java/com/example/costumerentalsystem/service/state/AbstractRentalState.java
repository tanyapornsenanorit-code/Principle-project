package com.example.costumerentalsystem.service.state;

import com.example.costumerentalsystem.domain.enums.RentalStatus;
import com.example.costumerentalsystem.exception.InvalidRentalTransitionException;

// ทุก action ไม่อนุญาตเป็นค่าเริ่มต้น state ไหนทำได้ก็ override เอาเอง
public abstract class AbstractRentalState implements RentalState {

    @Override
    public RentalStatus pay() {
        return reject("ชำระเงิน");
    }

    @Override
    public RentalStatus ship() {
        return reject("จัดส่ง");
    }

    @Override
    public RentalStatus startUse() {
        return reject("เริ่มใช้งาน");
    }

    @Override
    public RentalStatus returnItem() {
        return reject("รับคืน");
    }

    @Override
    public RentalStatus complete() {
        return reject("ปิดงาน");
    }

    @Override
    public RentalStatus cancel() {
        return reject("ยกเลิก");
    }

    protected RentalStatus reject(String action) {
        throw new InvalidRentalTransitionException(status(), action);
    }
}
