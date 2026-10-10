document.addEventListener("DOMContentLoaded", function () {
    const startDateInput = document.getElementById("startDate");
    const endDateInput = document.getElementById("endDate");

    if (startDateInput && endDateInput) {
        const calculateTotal = () => {
            const start = new Date(startDateInput.value);
            const end = new Date(endDateInput.value);

            if (start && end && end >= start) {
                const diffTime = Math.abs(end - start);
                const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) || 1;
                console.log("จำนวนวันที่เช่า:", diffDays);
            }
        };

        startDateInput.addEventListener("change", calculateTotal);
        endDateInput.addEventListener("change", calculateTotal);
    }
});