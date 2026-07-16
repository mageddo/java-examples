package com.mageddo.stock.entrypoint.resource;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeStockResourceIT extends StockResourceTest {

    // Execute the same tests but in native mode.
}
