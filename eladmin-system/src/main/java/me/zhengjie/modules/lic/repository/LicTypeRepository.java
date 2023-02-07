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
package me.zhengjie.modules.lic.repository;

import me.zhengjie.modules.lic.domain.LicType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @website https://eladmin.vip
* @author whwu
* @date 2023-02-07
**/
public interface LicTypeRepository extends JpaRepository<LicType, Long>, JpaSpecificationExecutor<LicType> {
    /**
    * 根据 TypeName 查询
    * @param type_name /
    * @return /
    */
    LicType findByTypeName(String type_name);
}