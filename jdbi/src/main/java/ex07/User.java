package ex07;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Builder
public class User {

  @Getter(onMethod_ = {@ColumnName("IDT_USER")})
  private int id;

  @Getter(onMethod_ = {@ColumnName("NAM_USER")})
  private String name;

  @Getter(onMethod_ = {@ColumnName("IND_GENDER")})
  private Gender gender;

  @Getter(onMethod_ = {@ColumnName("JSN_METADATA")})
  private Json metadata;

}
