/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.modules.lic.service.impl;

import me.zhengjie.modules.lic.domain.LicType;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.lic.repository.LicTypeRepository;
import me.zhengjie.modules.lic.service.LicTypeService;
import me.zhengjie.modules.lic.service.dto.LicTypeDto;
import me.zhengjie.modules.lic.service.dto.LicTypeQueryCriteria;
import me.zhengjie.modules.lic.service.mapstruct.LicTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author whwu
* @date 2023-02-07
**/
@Service
@RequiredArgsConstructor
public class LicTypeServiceImpl implements LicTypeService {

    private final LicTypeRepository licTypeRepository;
    private final LicTypeMapper licTypeMapper;

    @Override
    public Map<String,Object> queryAll(LicTypeQueryCriteria criteria, Pageable pageable){
        Page<LicType> page = licTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(licTypeMapper::toDto));
    }

    @Override
    public List<LicTypeDto> queryAll(LicTypeQueryCriteria criteria){
        return licTypeMapper.toDto(licTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public LicTypeDto findById(Long id) {
        LicType licType = licTypeRepository.findById(id).orElseGet(LicType::new);
        ValidationUtil.isNull(licType.getId(),"LicType","id",id);
        return licTypeMapper.toDto(licType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LicTypeDto create(LicType resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId()); 
        if(licTypeRepository.findByTypeName(resources.getTypeName()) != null){
            throw new EntityExistException(LicType.class,"type_name",resources.getTypeName());
        }
        return licTypeMapper.toDto(licTypeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(LicType resources) {
        LicType licType = licTypeRepository.findById(resources.getId()).orElseGet(LicType::new);
        ValidationUtil.isNull( licType.getId(),"LicType","id",resources.getId());
        LicType licType1 = null;
        licType1 = licTypeRepository.findByTypeName(resources.getTypeName());
        if(licType1 != null && !licType1.getId().equals(licType.getId())){
            throw new EntityExistException(LicType.class,"type_name",resources.getTypeName());
        }
        licType.copy(resources);
        licTypeRepository.save(licType);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            licTypeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<LicTypeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LicTypeDto licType : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("类型名称", licType.getTypeName());
            map.put("价格", licType.getPrice());
            map.put("创建时间", licType.getCreateTime());
            map.put("创建人", licType.getCreateBy());
            map.put("更新人", licType.getUpdateBy());
            map.put("更新时间", licType.getUpdateTime());
            map.put("最多使用次数", licType.getLimitUse());
            map.put("启用状态", licType.getEnabled());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}