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
import { Radio, FormControlLabel } from "@mui/material";
import * as OEQ from "@openequella/rest-api-client";
import * as React from "react";
import { WizardOptionGroup } from "./WizardOptionGroup";
import { WizardControlBasicProps } from "./WizardHelper";

export interface WizardRadioButtonGroupProps extends WizardControlBasicProps {
  /**
   * The list of options.
   */
  options: OEQ.WizardCommonTypes.WizardControlOption[];
  /**
   * The number of options displayed in one row.
   */
  columns: number;
  /**
   * The currently selected option.
   */
  value?: string;
  /**
   * Handler for selecting an option.
   */
  onSelect: (selectedValue: string) => void;
}

export const WizardRadioButtonGroup = (props: WizardRadioButtonGroupProps) => {
  const buildOption = ({
    text,
    value,
  }: OEQ.WizardCommonTypes.WizardControlOption): JSX.Element => (
    <FormControlLabel
      label={text}
      control={
        <Radio
          checked={props.value === value}
          value={value}
          onChange={(e) => props.onSelect(e.target.value)}
        />
      }
    />
  );

  return <WizardOptionGroup {...props} buildOption={buildOption} />;
};
