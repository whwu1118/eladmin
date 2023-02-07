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
import me.zhengjie.modules.lic.domain.LicType;
import me.zhengjie.modules.lic.service.LicTypeService;
import me.zhengjie.modules.lic.service.dto.LicTypeQueryCriteria;
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
* @date 2023-02-07
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "/api/lic/type管理")
@RequestMapping("/api/licType")
public class LicTypeController {

    private final LicTypeService licTypeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('licType:list')")
    public void exportLicType(HttpServletResponse response, LicTypeQueryCriteria criteria) throws IOException {
        licTypeService.download(licTypeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询/api/lic/type")
    @ApiOperation("查询/api/lic/type")
    @PreAuthorize("@el.check('licType:list')")
    public ResponseEntity<Object> queryLicType(LicTypeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(licTypeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增/api/lic/type")
    @ApiOperation("新增/api/lic/type")
    @PreAuthorize("@el.check('licType:add')")
    public ResponseEntity<Object> createLicType(@Validated @RequestBody LicType resources){
        return new ResponseEntity<>(licTypeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改/api/lic/type")
    @ApiOperation("修改/api/lic/type")
    @PreAuthorize("@el.check('licType:edit')")
    public ResponseEntity<Object> updateLicType(@Validated @RequestBody LicType resources){
        licTypeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除/api/lic/type")
    @ApiOperation("删除/api/lic/type")
    @PreAuthorize("@el.check('licType:del')")
    public ResponseEntity<Object> deleteLicType(@RequestBody Long[] ids) {
        licTypeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}