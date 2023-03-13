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
import { List } from "@mui/material";
import * as React from "react";
import { ReactNode } from "react";
import SettingsListHeading from "./SettingsListHeading";

export interface SettingsListProps {
  /**
   * Optional subheading. Appears above the list at the top left.
   */
  subHeading?: string;
  /**
   * The children of this component - should be zero or more SettingsListControls.
   */
  children: ReactNode;
}
/**
 * This component is used to define a settings list to be used in the page/settings/* pages.
 *
 */
export default function SettingsList({
  subHeading,
  children,
}: SettingsListProps) {
  return (
    <>
      {subHeading && <SettingsListHeading heading={subHeading} />}
      <List>{children}</List>
    </>
  );
}
