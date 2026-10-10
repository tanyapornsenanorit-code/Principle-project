package com.example.costumerentalsystem.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.example.costumerentalsystem.domain.entity.Category;
import com.example.costumerentalsystem.domain.entity.Costume;
import com.example.costumerentalsystem.domain.entity.Payment;
import com.example.costumerentalsystem.domain.entity.Rental;
import com.example.costumerentalsystem.domain.entity.User;
import com.example.costumerentalsystem.domain.entity.UserProfile;
import com.example.costumerentalsystem.domain.enums.CostumeStatus;
import com.example.costumerentalsystem.domain.enums.PaymentStatus;
import com.example.costumerentalsystem.domain.enums.RentalStatus;
import com.example.costumerentalsystem.domain.enums.Role;

/** ตรวจว่า mapping ของ Entity ทั้ง 7 ตัว (1-1, 1-N, cascade) ทำงานตามที่ออกแบบ */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class DomainMappingTest {

    @Autowired
    private TestEntityManager em;

    @Test
    void userAndProfile_areOneToOne_andProfileIsSavedByCascade() {
        User user = new User("somchai", "hashed", Role.USER);
        user.setFullName("Somchai Jaidee"); // สร้าง UserProfile ให้อัตโนมัติ
        user.setPhone("0812345678");

        em.persistAndFlush(user);
        em.clear();

        User loaded = em.find(User.class, user.getId());
        UserProfile profile = loaded.getProfile();
        assertThat(profile).isNotNull();
        assertThat(profile.getFullName()).isEqualTo("Somchai Jaidee");
        assertThat(profile.getUser().getId()).isEqualTo(loaded.getId());
        assertThat(loaded.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void categoryAndCostume_areOneToMany() {
        Category category = em.persist(new Category("ชุดไทย"));
        Costume c1 = em.persist(new Costume("ชุดไทยจิตรลดา", category, new BigDecimal("500.00"), "d", CostumeStatus.AVAILABLE, null));
        em.persist(new Costume("ชุดไทยจักรี", category, new BigDecimal("700.00"), "d", CostumeStatus.AVAILABLE, null));
        em.flush();
        em.clear();

        Category loaded = em.find(Category.class, category.getId());
        assertThat(loaded.getCostumes()).hasSize(2);
        assertThat(em.find(Costume.class, c1.getId()).getCategory().getName()).isEqualTo("ชุดไทย");
    }

    @Test
    void rental_hasOnePayment_andOneShipment() {
        User user = em.persist(new User("buyer", "hashed", Role.USER));
        Category category = em.persist(new Category("ชุดสูท"));
        Costume costume = em.persist(new Costume("สูทสีดำ", category, new BigDecimal("300.00"), "d", CostumeStatus.AVAILABLE, null));

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setCostume(costume);
        rental.setStartDate(LocalDate.of(2026, 10, 10));
        rental.setEndDate(LocalDate.of(2026, 10, 12));
        rental.setTotalDays(2);
        rental.setTotalPrice(new BigDecimal("600.00"));
        rental.setDepositAmount(new BigDecimal("150.00"));

        Payment payment = new Payment();
        payment.setAmount(new BigDecimal("600.00"));
        rental.setPayment(payment);

        rental.setCourier("Flash");      // ส่งต่อไปสร้าง Shipment
        rental.setTrackingNo("TH123456");

        em.persistAndFlush(rental);
        em.clear();

        Rental loaded = em.find(Rental.class, rental.getId());
        assertThat(loaded.getStatus()).isEqualTo(RentalStatus.PENDING_PAYMENT);
        assertThat(loaded.getCreatedAt()).isNotNull();
        assertThat(loaded.getPayment().getStatus()).isEqualTo(PaymentStatus.PENDING);
        assertThat(loaded.getShipment().getTrackingNo()).isEqualTo("TH123456");
        assertThat(loaded.getCourier()).isEqualTo("Flash");
    }
}
