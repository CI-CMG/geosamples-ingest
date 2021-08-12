package gov.noaa.ncei.mgg.geosamples.ingest.service;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

class SignificantField<T> {

  private final Supplier<T> getter1;
  private final Consumer<T> setter1;
  private final Supplier<T> getter2;
  private final Consumer<T> setter2;

  public SignificantField(Supplier<T> getter1, Consumer<T> setter1, Supplier<T> getter2, Consumer<T> setter2) {
    this.getter1 = getter1;
    this.setter1 = setter1;
    this.getter2 = getter2;
    this.setter2 = setter2;
  }

  public T get1() {
    return getter1.get();
  }

  public void set1(T value) {
    setter1.accept(value);
  }

  public T get2() {
    return getter2.get();
  }

  public void set2(T value) {
    setter2.accept(value);
  }

  public void copy1To2() {
    set2(get1());
  }

  public boolean isEqual() {
    return Objects.equals(get1(), get2());
  }
}
