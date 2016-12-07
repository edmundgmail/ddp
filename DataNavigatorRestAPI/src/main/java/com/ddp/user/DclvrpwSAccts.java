/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.ddp.user;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class DclvrpwSAccts extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"DclvrpwSAccts\",\"namespace\":\"com.ddp.user\",\"fields\":[{\"name\":\"transNum\",\"type\":\"string\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.lang.CharSequence transNum;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public DclvrpwSAccts() {}

  /**
   * All-args constructor.
   */
  public DclvrpwSAccts(java.lang.CharSequence transNum) {
    setTransNum(transNum);
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return transNum;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: transNum = (java.lang.CharSequence)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'transNum' field.
   */
  public java.lang.CharSequence getTransNum() {
    return transNum;
  }

  /**
   * Sets the value of the 'transNum' field.
   * @param value the value to set.
   */
  public void setTransNum(java.lang.CharSequence value) {
    this.transNum = value;
  }

  /** Creates a new DclvrpwSAccts RecordBuilder */
  public static com.ddp.user.DclvrpwSAccts.Builder newBuilder() {
    return new com.ddp.user.DclvrpwSAccts.Builder();
  }
  
  /** Creates a new DclvrpwSAccts RecordBuilder by copying an existing Builder */
  public static com.ddp.user.DclvrpwSAccts.Builder newBuilder(com.ddp.user.DclvrpwSAccts.Builder other) {
    return new com.ddp.user.DclvrpwSAccts.Builder(other);
  }
  
  /** Creates a new DclvrpwSAccts RecordBuilder by copying an existing DclvrpwSAccts instance */
  public static com.ddp.user.DclvrpwSAccts.Builder newBuilder(com.ddp.user.DclvrpwSAccts other) {
    return new com.ddp.user.DclvrpwSAccts.Builder(other);
  }
  
  /**
   * RecordBuilder for DclvrpwSAccts instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<DclvrpwSAccts>
    implements org.apache.avro.data.RecordBuilder<DclvrpwSAccts> {

    private java.lang.CharSequence transNum;

    /** Creates a new Builder */
    private Builder() {
      super(com.ddp.user.DclvrpwSAccts.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.ddp.user.DclvrpwSAccts.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.transNum)) {
        this.transNum = data().deepCopy(fields()[0].schema(), other.transNum);
        fieldSetFlags()[0] = true;
      }
    }
    
    /** Creates a Builder by copying an existing DclvrpwSAccts instance */
    private Builder(com.ddp.user.DclvrpwSAccts other) {
            super(com.ddp.user.DclvrpwSAccts.SCHEMA$);
      if (isValidValue(fields()[0], other.transNum)) {
        this.transNum = data().deepCopy(fields()[0].schema(), other.transNum);
        fieldSetFlags()[0] = true;
      }
    }

    /** Gets the value of the 'transNum' field */
    public java.lang.CharSequence getTransNum() {
      return transNum;
    }
    
    /** Sets the value of the 'transNum' field */
    public com.ddp.user.DclvrpwSAccts.Builder setTransNum(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.transNum = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'transNum' field has been set */
    public boolean hasTransNum() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'transNum' field */
    public com.ddp.user.DclvrpwSAccts.Builder clearTransNum() {
      transNum = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    @Override
    public DclvrpwSAccts build() {
      try {
        DclvrpwSAccts record = new DclvrpwSAccts();
        record.transNum = fieldSetFlags()[0] ? this.transNum : (java.lang.CharSequence) defaultValue(fields()[0]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}