package ${pojo.getPackageDeclaration()} ;

import lombok.*;

<#if pojo.importType("java.io.Serializable")??>
import java.io.Serializable;
</#if>

<#if pojo.importType("jakarta.persistence.*")??>
import jakarta.persistence.*;
</#if>

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "${pojo.getTableName()}")
public class ${pojo.getDeclarationName()} <#if pojo.importType("java.io.Serializable")??>implements Serializable</#if> {

<#list pojo.getAllPropertiesIterator() as property>
    @Column(name = "${property.getColumnName()}")
    private ${property.getJavaTypeName()} ${property.getName()};
</#list>

}