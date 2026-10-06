package com.example.costumerentalsystem.service.state;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.costumerentalsystem.domain.enums.RentalStatus;

// เอา RentalState ทุกตัวที่เป็น @Component มาใส่ map ไว้ค้นด้วย enum
@Component
public class RentalStateFactoryImpl implements RentalStateFactory {

    private final Map<RentalStatus, RentalState> states = new EnumMap<>(RentalStatus.class);

    public RentalStateFactoryImpl(List<RentalState> allStates) {
        for (RentalState state : allStates) {
            states.put(state.status(), state);
        }
    }

    @Override
    public RentalState stateOf(RentalStatus status) {
        RentalState state = states.get(status);
        if (state == null) {
            throw new IllegalStateException("ไม่มี RentalState สำหรับสถานะ " + status);
        }
        return state;
    }
}
