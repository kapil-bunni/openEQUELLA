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
import { Dialog, DialogContent, DialogTitle } from "@mui/material";
import { cloudProviderLangStrings } from "./CloudProviderModule";
import IconButton from "@mui/material/IconButton";
import CloseIcon from "@mui/icons-material/Close";
import Typography from "@mui/material/Typography";

const PREFIX = "CloudProviderDisclaimerDialog";

const classes = {
  closeButton: `${PREFIX}-closeButton`,
};

const StyledDialog = styled(Dialog)(({ theme }) => ({
  [`& .${classes.closeButton}`]: {
    position: "absolute",
    right: theme.spacing(1),
  },
}));

interface CloudProviderDisclaimerDialogProps {
  openDialog: boolean;
  onClose: () => void;
}

const CloudProviderDisclaimerDialog = ({
  openDialog,
  onClose,
}: CloudProviderDisclaimerDialogProps) => {
  return (
    <StyledDialog fullWidth open={openDialog} onClose={onClose}>
      <DialogTitle>
        {cloudProviderLangStrings.newcloudprovider.disclaimer.title}
        <IconButton
          aria-label="Close"
          onClick={onClose}
          className={classes.closeButton}
          size="large"
        >
          <CloseIcon />
        </IconButton>
      </DialogTitle>
      <DialogContent>
        <Typography align="justify">
          You acknowledge that the software, products and services{" "}
          <b>(Materials)</b> made available through the Cloud Provider Portal
          are provided to you by the relevant Cloud Provider and have not been
          authenticated or endorsed in whole or in part by any third party. Your
          use of the Materials is governed solely by the terms and conditions of
          the End User Licence or Services Agreement between you and the Cloud
          Provider <b>(Provider Agreement)</b>, if any, which accompanies or is
          included with the Materials. Except to the extent set forth in the
          applicable Provider Agreement, the Materials are provided “as is” and
          without any representation or warranties, express or implied,
          including but not limited to any representations or warranties as to
          merchantability, fitness for purpose, functionality, accuracy,
          availability, quality, completeness, validity or non-infringement of
          third party rights.
          <br />
          <br />
          You acknowledge that a Cloud Provider may gather, store and process
          certain technical information, account information, and metadata
          associated with your access and use of the Materials, including
          application telemetry, IP addresses, IP configurations, stored
          sessions, open ports, account credentials, network metadata, and
          device operating system, status, version and configuration
          (collectively <b>Metadata</b>). You further acknowledge that the Cloud
          Provider may gather, store and process all content, information,
          materials and intellectual property provided in its unaltered form by
          you in connection with your use of the Materials (<b>Your Data</b>)
          and that Your Data may include Personal Data as defined in Article 4
          of the European Union General Data Protection Regulation. You are
          solely responsible for your decision to permit a Cloud Provider to
          gather, store and process Metadata and Your Data. It is your
          responsibility to carefully review any Provider Agreement and satisfy
          yourself as to the sufficiency of the security and privacy practices
          of the relevant Cloud Provider before registering the Cloud Provider
          and receiving any Materials from it.
        </Typography>
      </DialogContent>
    </StyledDialog>
  );
};

export default CloudProviderDisclaimerDialog;
