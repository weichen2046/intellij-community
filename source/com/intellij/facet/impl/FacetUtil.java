/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */

package com.intellij.facet.impl;

import com.intellij.facet.*;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author nik
 */
public class FacetUtil {
  private FacetUtil() {
  }

  public static <F extends Facet, C extends FacetConfiguration> F createFacet(FacetType<F, C> type, Module module, final Facet underlying) {
    return FacetManagerImpl.createFacet(type, module, type.getDefaultFacetName(), type.createDefaultConfiguration(), underlying);
  }

  public static <F extends Facet> F addFacet(FacetType<F, ?> type, Module module, final Facet underlying) {
    final ModifiableFacetModel model = FacetManager.getInstance(module).createModifiableModel();
    final F facet = createFacet(type, module, underlying);
    model.addFacet(facet);
    model.commit();
    return facet;
  }
 
  public static void deleteFacet(final Facet facet) {
    new WriteAction() {
      protected void run(final Result result) {
        if (!isRegistered(facet)) {
          return;
        }

        ModifiableFacetModel model = FacetManager.getInstance(facet.getModule()).createModifiableModel();
        model.removeFacet(facet);
        model.commit();
      }
    }.execute();
  }

  public static boolean isRegistered(Facet facet) {
    return Arrays.asList(FacetManager.getInstance(facet.getModule()).getAllFacets()).contains(facet);
  }

  public static void deleteImplicitFacets(final Module module, final FacetTypeId typeId) {
    new WriteAction() {
      protected void run(final Result result) {
        ModifiableFacetModel model = FacetManager.getInstance(module).createModifiableModel();
        //noinspection unchecked
        Collection<Facet> facetsCollection = model.getFacetsByType(typeId);
        Facet[] facets = facetsCollection.toArray(new Facet[facetsCollection.size()]);
        for (Facet facet : facets) {
          if (facet.isImplicit()) {
            model.removeFacet(facet);
          }
        }
        model.commit();
      }
    }.execute();
  }

  public static void deleteImplicitFacets(Project project, final FacetTypeId typeId) {
    for (Module module : ModuleManager.getInstance(project).getModules()) {
      deleteImplicitFacets(module, typeId);
    }
  }
}
