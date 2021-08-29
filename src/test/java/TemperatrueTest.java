import org.example.Temperatrue;
import org.example.exception.DataNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sun.jvm.hotspot.utilities.Assert;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TemperatrueTest {
    private Temperatrue temperatrue;

    @BeforeEach
    void setUp() throws Exception {
        temperatrue = new Temperatrue();
    }

    @Test
    void testSuccess() throws Exception {
       Optional<String> temperature = temperatrue.getTemperature("江苏", "苏州", "吴中");
       assertNotNull(temperature);
       assertEquals(true, temperature.isPresent());
    }

    @Test
    void testIllegalArgsNullProvince() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> temperatrue.getTemperature(null, "苏州", "吴中"));
        assertEquals("please provide args properly.", exception.getMessage());
    }

    @Test
    void testIllegalArgsNullCity() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> temperatrue.getTemperature("江苏", null, "吴中"));
        assertEquals("please provide args properly.", exception.getMessage());
    }

    @Test
    void testIllegalArgsNullCountry() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> temperatrue.getTemperature("江苏", "苏州", null));
        assertEquals("please provide args properly.", exception.getMessage());
    }

    @Test
    void testIllegalArgsEmptyProvince() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> temperatrue.getTemperature("", "苏州", "吴中"));
        assertEquals("please provide args properly.", exception.getMessage());
    }

    @Test
    void testIllegalArgsEmptyCity() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> temperatrue.getTemperature("江苏", "", "吴中"));
        assertEquals("please provide args properly.", exception.getMessage());
    }

    @Test
    void testIllegalArgsEmptyCountry() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> temperatrue.getTemperature("江苏", "苏州", ""));
        assertEquals("please provide args properly.", exception.getMessage());
    }

    @Test
    void testProvinceNotFound() {
        Throwable exception = assertThrows(DataNotFoundException.class, () -> temperatrue.getTemperature("江苏苏", "苏州", "吴中"));
        assertEquals("province not found with name: 江苏苏", exception.getMessage());
    }

    @Test
    void testCityNotFound() {
        Throwable exception = assertThrows(DataNotFoundException.class, () -> temperatrue.getTemperature("江苏", "苏州shi", "吴中"));
        assertEquals("city not found city with name: 苏州shi", exception.getMessage());
    }

    @Test
    void testCountryNotFound() {
        Throwable exception = assertThrows(DataNotFoundException.class, () -> temperatrue.getTemperature("江苏", "苏州", "吴中中"));
        assertEquals("country not found with name: 吴中中", exception.getMessage());
    }
}
