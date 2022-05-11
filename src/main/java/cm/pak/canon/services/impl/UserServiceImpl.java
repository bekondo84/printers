package cm.pak.canon.services.impl;

import cm.pak.canon.beans.UserData;
import cm.pak.canon.models.Structure;
import cm.pak.canon.models.User;
import cm.pak.canon.repertories.StructureRepository;
import cm.pak.canon.repertories.UserRepository;
import cm.pak.canon.services.CSVService;
import cm.pak.canon.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private CSVService csvService ;

    @Autowired
    private StructureRepository structureRepository ;

    @Override
    public List<User> getUsers(int start, int max) {
        final List<User> datas = new ArrayList<>();
        userRepository.findAll()
                .forEach(user -> datas.add(user));
        //LOG.info(String.format("Nomber of users found : %s", datas.size()));
        return datas ;
    }

    @Override
    public void importUsers(MultipartFile mpf) throws IOException {
        final String[] headers = {"id", "name", "codeService","nameService", "codeStructure", "nameStructure"};
        final List<UserData> datas = csvService.parseCsv(mpf.getInputStream(), headers, new UserData());

        if (!CollectionUtils.isEmpty(datas)) {
            datas.stream()
                    .filter(d -> StringUtils.hasLength(d.getId()))
                    .forEach(userData -> {
                        final User user =  new User(userData.getId());
                        user.setName(userData.getName());
                        if (StringUtils.hasLength(userData.getCodeService())) {
                            final Structure affec = getAffectation(userData);
                            user.setAffectation(affec);
                        }
                        if (StringUtils.hasLength(userData.getCodeStructure())) {
                            final Structure str = getStructure(userData);
                            user.setStructure(str);
                        }
                        userRepository.save(user);
                    });
        }
    }

    private Structure getStructure(UserData userData) {
        Structure str =  structureRepository.findById(truncate(userData.getCodeStructure()))
                .orElse(null);
        if (Objects.isNull(str)) {
            str = new Structure(userData.getCodeStructure(), truncate(userData.getNameStructure()));
            str = structureRepository.save(str);
        } else if (StringUtils.hasLength(userData.getNameStructure())
                && !StringUtils.hasLength(str.getIntitule())){
            str.setIntitule(userData.getNameStructure());
            str = structureRepository.save(str);
        }
        return str;
    }

    private String truncate(String userData) {
        return userData.substring(0, userData.length()> 200 ? 200: userData.length());
    }

    private Structure getAffectation(UserData userData) {
        Structure str = structureRepository.findById(userData.getCodeService())
                .orElse(null);
        if (Objects.isNull(str)) {
            str = new Structure(userData.getCodeService(), truncate(userData.getNameService()));
            str = structureRepository.save(str);
        } else if(StringUtils.hasLength(userData.getNameService())
            && !StringUtils.hasLength(str.getIntitule())) {
            str.setIntitule(truncate(userData.getNameService()));
            str = structureRepository.save(str);
        }
        return str ;
    }
}
