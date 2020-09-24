// package es.uvigo.ei.sing.bam.dto.mapper;
//
// import es.uvigo.ei.sing.bam.dto.entity.RoleDto;
// import es.uvigo.ei.sing.bam.dto.entity.UserDto;
// import es.uvigo.ei.sing.bam.entity.UserEntity;
// import org.modelmapper.ModelMapper;
// import org.springframework.stereotype.Component;
//
// /**
//  * Created by Arpit Khandelwal.
//  */
// @Component
// public class UserMapper {
//
//     // Do not map the password
//     public static UserDto toUserDto(UserEntity userEntity) {
//         return new UserDto()
//                 .setUsername(userEntity.getUsername())
//                 .setEmail(userEntity.getEmail())
//                 .setFirstname(userEntity.getFirstname())
//                 .setSurname(userEntity.getSurname())
//                 .setCreated(userEntity.getCreated())
//                 .setRoleDto(new ModelMapper().map(userEntity.getRole(), RoleDto.class));
//     }
//
// }
