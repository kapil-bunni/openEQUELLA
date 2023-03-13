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
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
} from "@mui/material";
import * as OEQ from "@openequella/rest-api-client";
import * as React from "react";
import { useContext, useEffect, useState } from "react";
import { AppContext } from "../../../mainui/App";
import { getMIMETypesFromServer } from "../../../modules/MimeTypesModule";
import { validateMimeTypeName } from "../../../modules/SearchFilterSettingsModule";
import { commonString } from "../../../util/commonstrings";
import { addElement, deleteElement } from "../../../util/ImmutableArrayUtil";
import { languageStrings } from "../../../util/langstrings";
import MimeTypeList from "./MimeTypeList";

export interface MimeTypeFilterEditingDialogProps {
  /**
   * If true, the dialog will be shown.
   */
  open: boolean;
  /**
   * Fired when the dialog is closed.
   */
  onClose: () => void;
  /**
   * Fired when clicking the ADD or OK button.
   * @param filter The filter that has been added or edited
   */
  addOrUpdate: (filter: OEQ.SearchFilterSettings.MimeTypeFilter) => void;
  /**
   * The filter to be edited, or undefined if the action is to add a new filter.
   */
  mimeTypeFilter?: OEQ.SearchFilterSettings.MimeTypeFilter;
  /**
   * An async function that returns a list of MimeTypeEntry.
   */
  mimeTypeSupplier?: () => Promise<OEQ.MimeType.MimeTypeEntry[]>;
}

/**
 * Component that shows a dialog where users can add/edit a MIME type filter.
 */
const MimeTypeFilterEditingDialog = ({
  open,
  onClose,
  mimeTypeFilter,
  addOrUpdate,
  mimeTypeSupplier = getMIMETypesFromServer,
}: MimeTypeFilterEditingDialogProps) => {
  const searchFilterStrings =
    languageStrings.settings.searching.searchfiltersettings;

  const [mimeTypeEntries, setMimeTypeEntries] = useState<
    OEQ.MimeType.MimeTypeEntry[]
  >([]);
  // Used to store the name of a MIME type filter.
  const [filterName, setFilterName] = useState<string>(
    mimeTypeFilter ? mimeTypeFilter.name : ""
  );
  // Used to store the MIME types of a MIME type filter.
  const [selectedMimeTypes, setSelectedMimeTypes] = useState<string[]>(
    mimeTypeFilter ? mimeTypeFilter.mimeTypes : []
  );
  const { appErrorHandler } = useContext(AppContext);

  const isNameValid = validateMimeTypeName(filterName);

  useEffect(() => {
    mimeTypeSupplier()
      .then((mimeTypes) => setMimeTypeEntries(mimeTypes))
      .catch(appErrorHandler);
  }, [mimeTypeSupplier, appErrorHandler]);

  /**
   * If a MIME type is selected and it doesn't exist in the collection of selected MIME types,
   * then add it to the collection.
   * if a MIME type is unselected and it exists in the collection of selected MIME types,
   * then remove it from the collection.
   */
  const updateMimeTypeSelections = React.useCallback(
    (checked: boolean, mimeType: string) => {
      if (checked && selectedMimeTypes.indexOf(mimeType) < 0) {
        setSelectedMimeTypes(addElement(selectedMimeTypes, mimeType));
      } else if (!checked && selectedMimeTypes.indexOf(mimeType) > -1) {
        setSelectedMimeTypes(
          deleteElement(
            selectedMimeTypes,
            (type: string) => type === mimeType,
            1
          )
        );
      }
    },
    [selectedMimeTypes]
  );

  const onAddOrUpdate = () => {
    addOrUpdate({
      id: mimeTypeFilter?.id,
      name: filterName,
      mimeTypes: selectedMimeTypes,
    });
    onClose();
  };

  return (
    <Dialog open={open} fullWidth>
      <DialogTitle>
        {mimeTypeFilter ? searchFilterStrings.edit : searchFilterStrings.add}
      </DialogTitle>
      <DialogContent>
        <TextField
          margin="dense"
          label={searchFilterStrings.filternamelabel}
          value={filterName}
          required
          fullWidth
          onChange={(event) => setFilterName(event.target.value)}
          error={!!filterName && !isNameValid}
          variant="standard"
        />
        <MimeTypeList
          entries={mimeTypeEntries}
          onChange={updateMimeTypeSelections}
          selected={selectedMimeTypes}
        />
      </DialogContent>
      <DialogActions>
        <Button
          id="MimeTypeFilterEditingDialog_cancel"
          onClick={onClose}
          color="primary"
        >
          {commonString.action.cancel}
        </Button>
        <Button
          id="MimeTypeFilterEditingDialog_save"
          data-testid="MimeTypeFilterEditingDialog_save"
          onClick={onAddOrUpdate}
          color="primary"
          disabled={
            !filterName || !isNameValid || selectedMimeTypes.length === 0
          }
        >
          {mimeTypeFilter ? commonString.action.ok : commonString.action.add}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default MimeTypeFilterEditingDialog;
