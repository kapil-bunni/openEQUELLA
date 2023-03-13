/*
 * Licensed to The Apereo Foundation under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * The Apereo Foundation licenses this file to you under the Apache License,
 * Version 2.0, (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import { FormControl, MenuItem, OutlinedInput, Select } from "@mui/material";
import * as React from "react";
import { languageStrings } from "../../../util/langstrings";
import * as OEQ from "@openequella/rest-api-client";

export interface WebPageIndexSettingProps {
  disabled: boolean;
  value: OEQ.SearchSettings.ContentIndexLevel;
  setValue: (indexOption: OEQ.SearchSettings.ContentIndexLevel) => void;
}
export default function WebPageIndexSetting({
  disabled,
  value,
  setValue,
}: WebPageIndexSettingProps) {
  const contentIndexSettingsStrings =
    languageStrings.settings.searching.contentIndexSettings;
  return (
    <FormControl variant="outlined">
      <Select
        SelectDisplayProps={{ id: "_contentIndex" }}
        disabled={disabled}
        onChange={(event) =>
          setValue(event.target.value as OEQ.SearchSettings.ContentIndexLevel)
        }
        variant="outlined"
        value={value}
        autoWidth
        input={<OutlinedInput id="_contentIndex" />}
      >
        <MenuItem value={0}>{contentIndexSettingsStrings.option.none}</MenuItem>
        <MenuItem value={1}>
          {contentIndexSettingsStrings.option.webPage}
        </MenuItem>
        <MenuItem value={2}>
          {contentIndexSettingsStrings.option.secondaryPage}
        </MenuItem>
      </Select>
    </FormControl>
  );
}
