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
import * as React from "react";
import { FunctionComponent, ReactNode } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Button,
} from "@mui/material";
import { commonString } from "../util/commonstrings";

export interface ConfirmDialogProps {
  /**
   * Open the dialog when true.
   */
  open: boolean;
  /**
   * The title of the dialog.
   */
  title: string;
  /**
   * Contents displayed in the dialog.
   */
  children?: ReactNode;
  /**
   * Fired when click the Confirm button.
   */
  onConfirm: () => void;
  /**
   * Fired when click the Cancel button.
   */
  onCancel: () => void;
  /**
   * The text of the Confirm button.
   */
  confirmButtonText: string;
  /**
   * `true` to disable the confirmation button.
   */
  disableConfirmButton?: boolean;
}

const ConfirmDialog: FunctionComponent<ConfirmDialogProps> = ({
  open,
  title,
  children,
  onCancel,
  onConfirm,
  confirmButtonText,
  disableConfirmButton = false,
}: ConfirmDialogProps) => {
  const { cancel } = commonString.action;
  return (
    <Dialog
      open={open}
      onClose={onCancel}
      aria-labelledby="alert-dialog-title"
      aria-describedby="alert-dialog-description"
      fullWidth
    >
      <DialogTitle id="alert-dialog-title">{title}</DialogTitle>
      <DialogContent>
        <DialogContentText component="div" id="alert-dialog-description">
          {children}
        </DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button
          onClick={onCancel}
          color="secondary"
          id="confirm-dialog-cancel-button"
        >
          {cancel}
        </Button>
        <Button
          onClick={onConfirm}
          color="primary"
          id="confirm-dialog-confirm-button"
          autoFocus
          disabled={disableConfirmButton}
        >
          {confirmButtonText}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default ConfirmDialog;
