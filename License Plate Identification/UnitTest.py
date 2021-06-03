import unittest
import Main


class TestStringMethods(unittest.TestCase):

    # test function to test equality of two value
    def test_License_plate_identification1(self):
        # error message in case if test case got failed
        errorMessage = "First value and second value are not equal!"
        # asser
        # tEqual() to check equality of first & second value
        ans = Main.main("1.png")
        self.assertEqual(ans, "MCLRNF1", errorMessage)

    def test_License_plate_identification2(self):  # *******************************************
        # error message in case if test case got failed
        errorMessage = "First value and second value are not equal!"
        # assertEqual() to check equality of first & second value
        ans = Main.main("2.png")
        self.assertEqual(ans, "L0LATT", errorMessage)

    def test_License_plate_identification3(self):
        # error message in case if test case got failed
        errorMessage = "First value and second value are not equal!"
        # assertEqual() to check equality of first & second value
        ans = Main.main("3.png")
        self.assertEqual(ans, "RIPLS1", errorMessage)

    def test_License_plate_identification4(self):
        # error message in case if test case got failed
        errorMessage = "First value and second value are not equal!"
        # assertEqual() to check equality of first & second value
        ans = Main.main("5.png")
        self.assertEqual(ans, "NVSBLE", errorMessage)

    def test_License_plate_identification5(self):
        # error message in case if test case got failed
        errorMessage = "First value and second value are not equal!"
        # assertEqual() to check equality of first & second value
        ans = Main.main("6.png")
        self.assertEqual(ans, "NYSJ", errorMessage)

    def test_License_plate_identification6(self):
        # error message in case if test case got failed
        errorMessage = "First value and second value are not equal!"
        # assertEqual() to check equality of first & second value
        ans = Main.main("7.png")
        self.assertEqual(ans, "ANBY0ND", errorMessage)

    def test_License_plate_identification7(self):
        # error message in case if test case got failed
        errorMessage = "First value and second value are not equal!"
        # assertEqual() to check equality of first & second value
        ans = Main.main("9.png")
        self.assertEqual(ans, "PNYEXPS", errorMessage)

    def test_License_plate_identification8(self):
        # error message in case if test case got failed
        errorMessage = "First value and second value are not equal!"
        # assertEqual() to check equality of first & second value
        ans = Main.main("10.png")
        self.assertEqual(ans, "Z00MN65", errorMessage)

    def test_License_plate_identification9(self):
        # error message in case if test case got failed
        errorMessage = "First value and second value are not equal!"
        # assertEqual() to check equality of first & second value
        ans = Main.main("13.png")
        self.assertEqual(ans, "EZEY", errorMessage)

    def test_License_plate_identification10(self):
        # error message in case if test case got failed
        errorMessage = "First value and second value are not equal!"
        # assertEqual() to check equality of first & second value
        ans = Main.main("15.png")
        self.assertEqual(ans, "U8NTBAD", errorMessage)

    def test_License_plate_identification11(self):  # ***********************************
        # error message in case if test case got failed
        errorMessage = "First value and second value are not equal!"
        # assertEqual() to check equality of first & second value
        ans = Main.main("11.png")
        self.assertEqual(ans, "HBR5SH1T", errorMessage)

    def test_License_plate_identification12(self):  # ********************************
        # error message in case if test case got failed
        errorMessage = "First value and second value are not equal!"
        # assertEqual() to check equality of first & second value
        ans = Main.main("12.png")
        self.assertEqual(ans, "NYSJ", errorMessage)

    def test_License_plate_identification13(self):  # ********************************
        # error message in case if test case got failed
        errorMessage = "First value and second value are not equal!"
        # assertEqual() to check equality of first & second value
        ans = Main.main("28.png")
        self.assertEqual(ans, "Z333332", errorMessage)

    def test_License_plate_identification14(self):
        # error message in case if test case got failed
        errorMessage = "First value and second value are not equal!"
        # assertEqual() to check equality of first & second value
        ans = Main.main("14.png")
        self.assertEqual(ans, "NITESIY", errorMessage)

    def test_License_plate_identification15(self):
        # error message in case if test case got failed
        errorMessage = "First value and second value are not equal!"
        # assertEqual() to check equality of first & second value
        ans = Main.main("22.png")
        self.assertEqual(ans, "6930113", errorMessage)
