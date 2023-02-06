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

import me.zhengjie.modules.lic.domain.LicApp;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.lic.repository.LicAppRepository;
import me.zhengjie.modules.lic.service.LicAppService;
import me.zhengjie.modules.lic.service.dto.LicAppDto;
import me.zhengjie.modules.lic.service.dto.LicAppQueryCriteria;
import me.zhengjie.modules.lic.service.mapstruct.LicAppMapper;
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
* @date 2023-02-06
**/
@Service
@RequiredArgsConstructor
public class LicAppServiceImpl implements LicAppService {

    private final LicAppRepository licAppRepository;
    private final LicAppMapper licAppMapper;

    @Override
    public Map<String,Object> queryAll(LicAppQueryCriteria criteria, Pageable pageable){
        Page<LicApp> page = licAppRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(licAppMapper::toDto));
    }

    @Override
    public List<LicAppDto> queryAll(LicAppQueryCriteria criteria){
        return licAppMapper.toDto(licAppRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public LicAppDto findById(Long appId) {
        LicApp licApp = licAppRepository.findById(appId).orElseGet(LicApp::new);
        ValidationUtil.isNull(licApp.getAppId(),"LicApp","appId",appId);
        return licAppMapper.toDto(licApp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LicAppDto create(LicApp resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setAppId(snowflake.nextId()); 
        return licAppMapper.toDto(licAppRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(LicApp resources) {
        LicApp licApp = licAppRepository.findById(resources.getAppId()).orElseGet(LicApp::new);
        ValidationUtil.isNull( licApp.getAppId(),"LicApp","id",resources.getAppId());
        licApp.copy(resources);
        licAppRepository.save(licApp);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long appId : ids) {
            licAppRepository.deleteById(appId);
        }
    }

    @Override
    public void download(List<LicAppDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LicAppDto licApp : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("产品名称", licApp.getName());
            map.put("创建时间", licApp.getCreateTime());
            map.put("创建者", licApp.getCreateBy());
            map.put("更新者", licApp.getUpdateBy());
            map.put("更新时间", licApp.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}