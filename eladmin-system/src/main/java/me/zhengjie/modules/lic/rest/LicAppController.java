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
package me.zhengjie.modules.lic.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.modules.lic.domain.LicApp;
import me.zhengjie.modules.lic.service.LicAppService;
import me.zhengjie.modules.lic.service.dto.LicAppQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://eladmin.vip
* @author whwu
* @date 2023-02-06
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "/api/product管理")
@RequestMapping("/api/licApp")
public class LicAppController {

    private final LicAppService licAppService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('licApp:list')")
    public void exportLicApp(HttpServletResponse response, LicAppQueryCriteria criteria) throws IOException {
        licAppService.download(licAppService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询/api/product")
    @ApiOperation("查询/api/product")
    @PreAuthorize("@el.check('licApp:list')")
    public ResponseEntity<Object> queryLicApp(LicAppQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(licAppService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增/api/product")
    @ApiOperation("新增/api/product")
    @PreAuthorize("@el.check('licApp:add')")
    public ResponseEntity<Object> createLicApp(@Validated @RequestBody LicApp resources){
        return new ResponseEntity<>(licAppService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改/api/product")
    @ApiOperation("修改/api/product")
    @PreAuthorize("@el.check('licApp:edit')")
    public ResponseEntity<Object> updateLicApp(@Validated @RequestBody LicApp resources){
        licAppService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除/api/product")
    @ApiOperation("删除/api/product")
    @PreAuthorize("@el.check('licApp:del')")
    public ResponseEntity<Object> deleteLicApp(@RequestBody Long[] ids) {
        licAppService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}