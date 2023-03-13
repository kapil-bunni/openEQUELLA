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
import { styled } from "@mui/material/styles";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  TextField,
  Typography,
} from "@mui/material";
import { cloudProviderLangStrings } from "./CloudProviderModule";
import { commonString } from "../util/commonstrings";
import Link from "@mui/material/Link";
import CloudProviderDisclaimerDialog from "./CloudProviderDisclaimerDialog";

const StyledTypography = styled(Typography)(({ theme }) => ({
  marginTop: theme.spacing(1),
}));

const StyledLink = styled(Link)({
  cursor: "pointer",
});

interface CloudProviderAddDialogProps {
  open: boolean;
  onCancel: () => void;
  onRegister: (url: string) => void;
}
interface CloudProviderAddDialogState {
  cloudProviderUrl: string;
  disclaimerDialogOpen: boolean;
}

class CloudProviderAddDialog extends React.Component<
  CloudProviderAddDialogProps,
  CloudProviderAddDialogState
> {
  constructor(props: CloudProviderAddDialogProps) {
    super(props);
    this.state = {
      cloudProviderUrl: "",
      disclaimerDialogOpen: false,
    };
  }

  handleTextChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    this.setState({
      cloudProviderUrl: e.target.value,
    });
  };

  validateUrl = (): boolean => {
    const url = this.state.cloudProviderUrl;
    if (url === "") {
      return true;
    } else {
      return url.search(/^[Hh][Tt][Tt][Pp]([Ss]?):\/\//) === 0;
    }
  };

  openDisclaimerDialog = () => {
    this.setState({
      disclaimerDialogOpen: true,
    });
  };

  closeDisclaimerDialog = () => {
    this.setState({
      disclaimerDialogOpen: false,
    });
  };

  render() {
    const { open, onCancel, onRegister } = this.props;
    const { cloudProviderUrl, disclaimerDialogOpen } = this.state;
    const isUrlValid = this.validateUrl();
    return (
      <div>
        <Dialog
          open={open}
          onClose={onCancel}
          aria-labelledby="form-dialog-title"
          disableEscapeKeyDown
          fullWidth
        >
          <DialogTitle>
            {cloudProviderLangStrings.newcloudprovider.title}
          </DialogTitle>
          <DialogContent>
            <DialogContentText>
              {cloudProviderLangStrings.newcloudprovider.text}
            </DialogContentText>
            <TextField
              autoFocus
              margin="dense"
              id="new_cloud_provider_url"
              label={cloudProviderLangStrings.newcloudprovider.label}
              value={cloudProviderUrl}
              required
              fullWidth
              onChange={this.handleTextChange}
              error={!isUrlValid}
              helperText={cloudProviderLangStrings.newcloudprovider.help}
            />

            <StyledTypography variant="body2">
              {cloudProviderLangStrings.newcloudprovider.disclaimer.text}
              <StyledLink
                underline="always"
                onClick={this.openDisclaimerDialog}
              >
                <b>
                  {cloudProviderLangStrings.newcloudprovider.disclaimer.title}
                </b>
              </StyledLink>
            </StyledTypography>
          </DialogContent>
          <DialogActions>
            <Button id="cancel-register" onClick={onCancel} color="primary">
              {commonString.action.cancel}
            </Button>
            <Button
              id="confirm-register"
              onClick={() => {
                onRegister(cloudProviderUrl);
              }}
              color="primary"
              disabled={!cloudProviderUrl || !isUrlValid}
            >
              {commonString.action.register}
            </Button>
          </DialogActions>
        </Dialog>

        <CloudProviderDisclaimerDialog
          openDialog={disclaimerDialogOpen}
          onClose={this.closeDisclaimerDialog}
        />
      </div>
    );
  }
}

export default CloudProviderAddDialog;
