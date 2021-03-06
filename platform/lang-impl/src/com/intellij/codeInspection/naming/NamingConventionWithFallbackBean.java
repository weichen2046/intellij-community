// Copyright 2000-2017 JetBrains s.r.o.
// Use of this source code is governed by the Apache 2.0 license that can be
// found in the LICENSE file.
package com.intellij.codeInspection.naming;

import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;

public class NamingConventionWithFallbackBean extends NamingConventionBean {
  public boolean inheritDefaultSettings = false;

  public NamingConventionWithFallbackBean(String regex, int minLength, int maxLength, String... predefinedNames) {
    super(regex, minLength, maxLength, predefinedNames);
  }

  public boolean isInheritDefaultSettings() {
    return inheritDefaultSettings;
  }

  @Override
  public JComponent createOptionsPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    JComponent selfOptions = super.createOptionsPanel();
    JCheckBox inheritCb = new JCheckBox("Use settings of class naming conventions", inheritDefaultSettings);
    panel.add(inheritCb, BorderLayout.NORTH);
    inheritCb.addActionListener(e -> {
      inheritDefaultSettings = inheritCb.isSelected();
      UIUtil.setEnabled(selfOptions, !inheritDefaultSettings, true);
    });
    panel.add(selfOptions, BorderLayout.CENTER);
    UIUtil.setEnabled(selfOptions, !inheritDefaultSettings, true);
    return panel;
  }
}
