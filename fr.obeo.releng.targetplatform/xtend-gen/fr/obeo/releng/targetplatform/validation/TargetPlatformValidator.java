/**
 * generated by Xtext
 */
package fr.obeo.releng.targetplatform.validation;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import fr.obeo.releng.targetplatform.targetplatform.IU;
import fr.obeo.releng.targetplatform.targetplatform.IncludeDeclaration;
import fr.obeo.releng.targetplatform.targetplatform.Location;
import fr.obeo.releng.targetplatform.targetplatform.Option;
import fr.obeo.releng.targetplatform.targetplatform.TargetPlatform;
import fr.obeo.releng.targetplatform.targetplatform.TargetplatformPackage;
import fr.obeo.releng.targetplatform.util.LocationIndexBuilder;
import fr.obeo.releng.targetplatform.validation.AbstractTargetPlatformValidator;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.AbstractRule;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.impl.CompositeNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.scoping.impl.ImportUriResolver;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * Custom validation rules.
 * 
 * see http://www.eclipse.org/Xtext/documentation.html#validation
 */
@SuppressWarnings("all")
public class TargetPlatformValidator extends AbstractTargetPlatformValidator {
  @Inject
  private ImportUriResolver resolver;
  
  private final LocationIndexBuilder indexBuilder;
  
  public final static String CHECK__OPTIONS_SELF_EXCLUDING_ALL_ENV_REQUIRED = "CHECK__OPTIONS_SELF_EXCLUDING_ALL_ENV_REQUIRED";
  
  public final static String CHECK__OPTIONS_EQUALS_ALL_LOCATIONS = "CHECK__OPTIONS_EQUALS_ALL_LOCATIONS";
  
  public final static String CHECK__NO_OPTIONS_ON_LOCATIONS_IF_GLOBAL_OPTIONS = "CHECK__NO_OPTIONS_ON_LOCATIONS_IF_GLOBAL_OPTIONS";
  
  public final static String DEPRECATE__OPTIONS_ON_LOCATIONS = "DEPRECATE__OPTIONS_ON_LOCATIONS";
  
  public final static String DEPRECATE__STRINGS_ON_IU_VERSION = "DEPRECATE__STRINGS_ON_IU_VERSION";
  
  public final static String CHECK__LOCATION_CONFLICTUAL_ID = "CHECK__LOCATION_CONFLICTUAL_ID";
  
  public final static String CHECK__INCLUDED_LOCATION_CONFLICTUAL_ID = "CHECK__INCLUDED_LOCATION_CONFLICTUAL_ID";
  
  public TargetPlatformValidator() {
    LocationIndexBuilder _locationIndexBuilder = new LocationIndexBuilder(this.resolver);
    this.indexBuilder = _locationIndexBuilder;
  }
  
  @Check
  public void checkAllEnvAndRequiredAreSelfExluding(final TargetPlatform targetPlatform) {
    EList<Option> _options = targetPlatform.getOptions();
    this.doCheckAllEnvAndRequiredAreSelfExluding(targetPlatform, _options, TargetplatformPackage.Literals.TARGET_PLATFORM__OPTIONS);
  }
  
  public void doCheckAllEnvAndRequiredAreSelfExluding(final EObject optionOwner, final List<Option> options, final EStructuralFeature feature) {
    boolean _and = false;
    boolean _contains = options.contains(Option.INCLUDE_ALL_ENVIRONMENTS);
    if (!_contains) {
      _and = false;
    } else {
      boolean _contains_1 = options.contains(Option.INCLUDE_REQUIRED);
      _and = _contains_1;
    }
    if (_and) {
      int _indexOf = options.indexOf(Option.INCLUDE_REQUIRED);
      this.error("All environments can not be included along with required artifacts, you must choose one of the two options.", optionOwner, feature, _indexOf, TargetPlatformValidator.CHECK__OPTIONS_SELF_EXCLUDING_ALL_ENV_REQUIRED);
      int _indexOf_1 = options.indexOf(Option.INCLUDE_ALL_ENVIRONMENTS);
      this.error("All environments can not be included along with required artifacts, you must choose one of the two options.", optionOwner, feature, _indexOf_1, TargetPlatformValidator.CHECK__OPTIONS_SELF_EXCLUDING_ALL_ENV_REQUIRED);
    }
  }
  
  @Check
  public void noLocationOptionIfGlobalOptions(final Location location) {
    boolean _and = false;
    EList<Option> _options = location.getOptions();
    boolean _isEmpty = _options.isEmpty();
    boolean _not = (!_isEmpty);
    if (!_not) {
      _and = false;
    } else {
      EObject _eContainer = location.eContainer();
      EList<Option> _options_1 = ((TargetPlatform) _eContainer).getOptions();
      boolean _isEmpty_1 = _options_1.isEmpty();
      boolean _not_1 = (!_isEmpty_1);
      _and = _not_1;
    }
    if (_and) {
      final List<INode> nodes = NodeModelUtils.findNodesForFeature(location, TargetplatformPackage.Literals.LOCATION__OPTIONS);
      INode _head = IterableExtensions.<INode>head(nodes);
      final INode withKeyword = ((CompositeNode) _head).getPreviousSibling();
      INode _last = IterableExtensions.<INode>last(nodes);
      final CompositeNode lastOption = ((CompositeNode) _last);
      int _offset = withKeyword.getOffset();
      int _endOffset = lastOption.getEndOffset();
      int _offset_1 = withKeyword.getOffset();
      int _minus = (_endOffset - _offset_1);
      this.acceptError("You can not define options on location and on target platform.", location, _offset, _minus, TargetPlatformValidator.CHECK__NO_OPTIONS_ON_LOCATIONS_IF_GLOBAL_OPTIONS);
    }
  }
  
  @Check
  public void checkOptionsOnLocationAreIdentical(final TargetPlatform targetPlatform) {
    EList<Option> _options = targetPlatform.getOptions();
    boolean _isEmpty = _options.isEmpty();
    if (_isEmpty) {
      final EList<Location> listOptions = targetPlatform.getLocations();
      final Location first = IterableExtensions.<Location>head(listOptions);
      Iterable<Location> _tail = IterableExtensions.<Location>tail(listOptions);
      final Function1<Location,Boolean> _function = new Function1<Location,Boolean>() {
        public Boolean apply(final Location _) {
          EList<Option> _options = _.getOptions();
          Set<Option> _set = IterableExtensions.<Option>toSet(_options);
          EList<Option> _options_1 = first.getOptions();
          Set<Option> _set_1 = IterableExtensions.<Option>toSet(_options_1);
          Sets.SetView<Option> _symmetricDifference = Sets.<Option>symmetricDifference(_set, _set_1);
          boolean _isEmpty = _symmetricDifference.isEmpty();
          return Boolean.valueOf((!_isEmpty));
        }
      };
      final Iterable<Location> conflicts = IterableExtensions.<Location>filter(_tail, _function);
      boolean _isEmpty_1 = IterableExtensions.isEmpty(conflicts);
      boolean _not = (!_isEmpty_1);
      if (_not) {
        final Procedure1<Location> _function_1 = new Procedure1<Location>() {
          public void apply(final Location _) {
            final List<INode> nodes = NodeModelUtils.findNodesForFeature(_, TargetplatformPackage.Literals.LOCATION__OPTIONS);
            boolean _isEmpty = nodes.isEmpty();
            boolean _not = (!_isEmpty);
            if (_not) {
              INode _head = IterableExtensions.<INode>head(nodes);
              final INode withKeyword = ((CompositeNode) _head).getPreviousSibling();
              INode _last = IterableExtensions.<INode>last(nodes);
              final CompositeNode lastOption = ((CompositeNode) _last);
              int _offset = withKeyword.getOffset();
              int _endOffset = lastOption.getEndOffset();
              int _offset_1 = withKeyword.getOffset();
              int _minus = (_endOffset - _offset_1);
              TargetPlatformValidator.this.acceptError("Options of every locations must be the same", _, _offset, _minus, TargetPlatformValidator.CHECK__OPTIONS_EQUALS_ALL_LOCATIONS);
            } else {
              final ICompositeNode node = NodeModelUtils.getNode(_);
              int _offset_2 = node.getOffset();
              int _length = node.getLength();
              TargetPlatformValidator.this.acceptError("Options of every locations must be the same", _, _offset_2, _length, TargetPlatformValidator.CHECK__OPTIONS_EQUALS_ALL_LOCATIONS);
            }
          }
        };
        IterableExtensions.<Location>forEach(listOptions, _function_1);
      }
    }
  }
  
  @Check
  public void checkAllEnvAndRequiredAreSelfExluding(final Location location) {
    EList<Option> _options = location.getOptions();
    this.doCheckAllEnvAndRequiredAreSelfExluding(location, _options, TargetplatformPackage.Literals.LOCATION__OPTIONS);
  }
  
  @Check
  public void deprecateOptionsOnLocation(final Location location) {
    EObject _eContainer = location.eContainer();
    final TargetPlatform targetPlatform = ((TargetPlatform) _eContainer);
    EList<Option> _options = targetPlatform.getOptions();
    boolean _isEmpty = _options.isEmpty();
    if (_isEmpty) {
      final List<INode> nodes = NodeModelUtils.findNodesForFeature(location, TargetplatformPackage.Literals.LOCATION__OPTIONS);
      INode _head = IterableExtensions.<INode>head(nodes);
      final INode withKeyword = ((CompositeNode) _head).getPreviousSibling();
      INode _last = IterableExtensions.<INode>last(nodes);
      final CompositeNode lastOption = ((CompositeNode) _last);
      int _offset = withKeyword.getOffset();
      int _endOffset = lastOption.getEndOffset();
      int _offset_1 = withKeyword.getOffset();
      int _minus = (_endOffset - _offset_1);
      this.acceptWarning("Options on location are deprecated. Define the option at the target level.", location, _offset, _minus, TargetPlatformValidator.DEPRECATE__OPTIONS_ON_LOCATIONS);
    }
  }
  
  @Check
  public void deprecateIUVersionRangeWihString(final IU iu) {
    String _version = iu.getVersion();
    boolean _notEquals = (!Objects.equal(_version, null));
    if (_notEquals) {
      final List<INode> nodes = NodeModelUtils.findNodesForFeature(iu, TargetplatformPackage.Literals.IU__VERSION);
      INode _head = IterableExtensions.<INode>head(nodes);
      EObject _grammarElement = _head.getGrammarElement();
      AbstractRule _rule = ((RuleCall) _grammarElement).getRule();
      String _name = _rule.getName();
      boolean _equals = "STRING".equals(_name);
      if (_equals) {
        this.warning("Usage of strings is deprecated for version range. You should remove the quotes.", iu, 
          TargetplatformPackage.Literals.IU__VERSION, 
          TargetPlatformValidator.DEPRECATE__STRINGS_ON_IU_VERSION);
      }
    }
  }
  
  @Check
  public void checkIDUniqueOnAllLocations(final TargetPlatform targetPlatform) {
    final ArrayListMultimap<String,Location> index = this.indexBuilder.getLocationIndex(targetPlatform);
    final Resource resource = targetPlatform.eResource();
    Set<String> _keySet = index.keySet();
    final Function1<String,Set<String>> _function = new Function1<String,Set<String>>() {
      public Set<String> apply(final String it) {
        List<Location> _get = index.get(it);
        final Function1<Location,Boolean> _function = new Function1<Location,Boolean>() {
          public Boolean apply(final Location it) {
            String _iD = it.getID();
            return Boolean.valueOf((!Objects.equal(_iD, null)));
          }
        };
        Iterable<Location> _filter = IterableExtensions.<Location>filter(_get, _function);
        final Function1<Location,String> _function_1 = new Function1<Location,String>() {
          public String apply(final Location it) {
            return it.getID();
          }
        };
        Iterable<String> _map = IterableExtensions.<Location, String>map(_filter, _function_1);
        return IterableExtensions.<String>toSet(_map);
      }
    };
    Iterable<Set<String>> _map = IterableExtensions.<String, Set<String>>map(_keySet, _function);
    final Function1<Set<String>,Boolean> _function_1 = new Function1<Set<String>,Boolean>() {
      public Boolean apply(final Set<String> it) {
        int _size = it.size();
        return Boolean.valueOf((_size > 1));
      }
    };
    final Iterable<Set<String>> conflictualURI = IterableExtensions.<Set<String>>filter(_map, _function_1);
    boolean _isEmpty = IterableExtensions.isEmpty(conflictualURI);
    if (_isEmpty) {
      Set<String> _keySet_1 = index.keySet();
      final Function1<String,Location> _function_2 = new Function1<String,Location>() {
        public Location apply(final String it) {
          List<Location> _get = index.get(it);
          final Function1<Location,Boolean> _function = new Function1<Location,Boolean>() {
            public Boolean apply(final Location it) {
              String _iD = it.getID();
              return Boolean.valueOf((!Objects.equal(_iD, null)));
            }
          };
          Iterable<Location> _filter = IterableExtensions.<Location>filter(_get, _function);
          return IterableExtensions.<Location>head(_filter);
        }
      };
      Iterable<Location> _map_1 = IterableExtensions.<String, Location>map(_keySet_1, _function_2);
      Iterable<Location> _filterNull = IterableExtensions.<Location>filterNull(_map_1);
      final Function<Location,String> _function_3 = new Function<Location,String>() {
        public String apply(final Location it) {
          return it.getID();
        }
      };
      final ImmutableListMultimap<String,Location> uniqueLocation = Multimaps.<String, Location>index(_filterNull, _function_3);
      uniqueLocation.keys();
      ImmutableMultiset<String> _keys = uniqueLocation.keys();
      final Function1<String,Boolean> _function_4 = new Function1<String,Boolean>() {
        public Boolean apply(final String it) {
          ImmutableMultiset<String> _keys = uniqueLocation.keys();
          int _count = _keys.count(it);
          return Boolean.valueOf((_count > 1));
        }
      };
      final Iterable<String> duplicateIDLocation = IterableExtensions.<String>filter(_keys, _function_4);
      final Procedure1<String> _function_5 = new Procedure1<String>() {
        public void apply(final String it) {
          ImmutableList<Location> _get = uniqueLocation.get(it);
          final Procedure1<Location> _function = new Procedure1<Location>() {
            public void apply(final Location duplicateID) {
              String _uri = duplicateID.getUri();
              final List<Location> allNonUniqIDLocations = index.get(_uri);
              final Function1<Location,Boolean> _function = new Function1<Location,Boolean>() {
                public Boolean apply(final Location it) {
                  Resource _eResource = it.eResource();
                  return Boolean.valueOf((!Objects.equal(resource, _eResource)));
                }
              };
              final Iterable<Location> external = IterableExtensions.<Location>filter(allNonUniqIDLocations, _function);
              final Function1<Location,Boolean> _function_1 = new Function1<Location,Boolean>() {
                public Boolean apply(final Location it) {
                  Resource _eResource = it.eResource();
                  return Boolean.valueOf(Objects.equal(resource, _eResource));
                }
              };
              final Iterable<Location> internal = IterableExtensions.<Location>filter(allNonUniqIDLocations, _function_1);
              final Procedure1<Location> _function_2 = new Procedure1<Location>() {
                public void apply(final Location it) {
                  TargetPlatformValidator.this.error("ID must be unique for each location", it, TargetplatformPackage.Literals.LOCATION__ID);
                }
              };
              IterableExtensions.<Location>forEach(internal, _function_2);
              final Procedure1<Location> _function_3 = new Procedure1<Location>() {
                public void apply(final Location location) {
                  EList<IncludeDeclaration> _includes = targetPlatform.getIncludes();
                  final Function1<IncludeDeclaration,Boolean> _function = new Function1<IncludeDeclaration,Boolean>() {
                    public Boolean apply(final IncludeDeclaration it) {
                      boolean _xblockexpression = false;
                      {
                        final TargetPlatform direct = TargetPlatformValidator.this.indexBuilder.getImportedTargetPlatform(resource, it);
                        boolean _or = false;
                        EList<Location> _locations = direct.getLocations();
                        boolean _contains = _locations.contains(location);
                        if (_contains) {
                          _or = true;
                        } else {
                          Collection<TargetPlatform> _importedTargetPlatforms = TargetPlatformValidator.this.indexBuilder.getImportedTargetPlatforms(direct);
                          final Function1<TargetPlatform,EList<Location>> _function = new Function1<TargetPlatform,EList<Location>>() {
                            public EList<Location> apply(final TargetPlatform it) {
                              return it.getLocations();
                            }
                          };
                          Iterable<EList<Location>> _map = IterableExtensions.<TargetPlatform, EList<Location>>map(_importedTargetPlatforms, _function);
                          Iterable<Location> _flatten = Iterables.<Location>concat(_map);
                          Set<Location> _set = IterableExtensions.<Location>toSet(_flatten);
                          boolean _contains_1 = _set.contains(location);
                          _or = _contains_1;
                        }
                        _xblockexpression = _or;
                      }
                      return Boolean.valueOf(_xblockexpression);
                    }
                  };
                  final IncludeDeclaration includeErr = IterableExtensions.<IncludeDeclaration>findFirst(_includes, _function);
                  StringConcatenation _builder = new StringConcatenation();
                  _builder.append("ID \'");
                  String _iD = duplicateID.getID();
                  _builder.append(_iD, "");
                  _builder.append("\' is duplicated in the included target platform");
                  TargetPlatformValidator.this.error(_builder.toString(), includeErr, TargetplatformPackage.Literals.INCLUDE_DECLARATION__IMPORT_URI);
                }
              };
              IterableExtensions.<Location>forEach(external, _function_3);
            }
          };
          IterableExtensions.<Location>forEach(_get, _function);
        }
      };
      IterableExtensions.<String>forEach(duplicateIDLocation, _function_5);
    }
  }
  
  @Check
  public void checkImportCycle(final TargetPlatform targetPlatform) {
    final List<TargetPlatform> cycle = this.indexBuilder.checkIncludeCycle(targetPlatform);
    boolean _isEmpty = cycle.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      EList<IncludeDeclaration> _includes = targetPlatform.getIncludes();
      final Function1<IncludeDeclaration,Boolean> _function = new Function1<IncludeDeclaration,Boolean>() {
        public Boolean apply(final IncludeDeclaration it) {
          TargetPlatform _get = cycle.get(1);
          Resource _eResource = targetPlatform.eResource();
          TargetPlatform _importedTargetPlatform = TargetPlatformValidator.this.indexBuilder.getImportedTargetPlatform(_eResource, it);
          return Boolean.valueOf(_get.equals(_importedTargetPlatform));
        }
      };
      final IncludeDeclaration cyclingImport = IterableExtensions.<IncludeDeclaration>findFirst(_includes, _function);
      boolean _notEquals = (!Objects.equal(cyclingImport, null));
      if (_notEquals) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("Cycle detected in the included target platforms. Cycle is \'");
        Iterable<TargetPlatform> _drop = IterableExtensions.<TargetPlatform>drop(cycle, 1);
        final Function1<TargetPlatform,URI> _function_1 = new Function1<TargetPlatform,URI>() {
          public URI apply(final TargetPlatform it) {
            Resource _eResource = it.eResource();
            return _eResource.getURI();
          }
        };
        Iterable<URI> _map = IterableExtensions.<TargetPlatform, URI>map(_drop, _function_1);
        String _join = IterableExtensions.join(_map, "\'\' -> \'");
        _builder.append(_join, "");
        _builder.append("\'.");
        this.error(_builder.toString(), cyclingImport, 
          TargetplatformPackage.Literals.INCLUDE_DECLARATION__IMPORT_URI);
      }
    }
  }
  
  @Check
  public void checkImportedLocationConflictualID(final TargetPlatform targetPlatform) {
    final ArrayListMultimap<String,Location> index = this.indexBuilder.getLocationIndex(targetPlatform);
    final Resource resource = targetPlatform.eResource();
    Set<String> _keySet = index.keySet();
    for (final String locURI : _keySet) {
      {
        List<Location> _get = index.get(locURI);
        final Function1<Location,Boolean> _function = new Function1<Location,Boolean>() {
          public Boolean apply(final Location it) {
            Resource _eResource = it.eResource();
            return Boolean.valueOf((!Objects.equal(_eResource, resource)));
          }
        };
        final Iterable<Location> externalLocations = IterableExtensions.<Location>filter(_get, _function);
        final Function1<Location,Boolean> _function_1 = new Function1<Location,Boolean>() {
          public Boolean apply(final Location it) {
            String _iD = it.getID();
            return Boolean.valueOf((!Objects.equal(_iD, null)));
          }
        };
        Iterable<Location> _filter = IterableExtensions.<Location>filter(externalLocations, _function_1);
        final Function1<Location,String> _function_2 = new Function1<Location,String>() {
          public String apply(final Location it) {
            return it.getID();
          }
        };
        Iterable<String> _map = IterableExtensions.<Location, String>map(_filter, _function_2);
        final Set<String> externalIDs = IterableExtensions.<String>toSet(_map);
        List<Location> _get_1 = index.get(locURI);
        final Function1<Location,Boolean> _function_3 = new Function1<Location,Boolean>() {
          public Boolean apply(final Location it) {
            Resource _eResource = it.eResource();
            return Boolean.valueOf(Objects.equal(_eResource, resource));
          }
        };
        final Iterable<Location> internalLocations = IterableExtensions.<Location>filter(_get_1, _function_3);
        final Function1<Location,Boolean> _function_4 = new Function1<Location,Boolean>() {
          public Boolean apply(final Location it) {
            String _iD = it.getID();
            return Boolean.valueOf((!Objects.equal(_iD, null)));
          }
        };
        Iterable<Location> _filter_1 = IterableExtensions.<Location>filter(internalLocations, _function_4);
        final Function1<Location,String> _function_5 = new Function1<Location,String>() {
          public String apply(final Location it) {
            return it.getID();
          }
        };
        Iterable<String> _map_1 = IterableExtensions.<Location, String>map(_filter_1, _function_5);
        final Set<String> internalIDs = IterableExtensions.<String>toSet(_map_1);
        int _size = externalIDs.size();
        boolean _greaterThan = (_size > 1);
        if (_greaterThan) {
          final Function1<Location,Boolean> _function_6 = new Function1<Location,Boolean>() {
            public Boolean apply(final Location it) {
              String _iD = it.getID();
              return Boolean.valueOf(externalIDs.contains(_iD));
            }
          };
          final Iterable<Location> externalLocationsWithConflictualID = IterableExtensions.<Location>filter(externalLocations, _function_6);
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("The ID for location \'");
          _builder.append(locURI, "");
          _builder.append("\' must be unique. Found \'");
          String _join = IterableExtensions.join(externalIDs, "\', \'");
          _builder.append(_join, "");
          _builder.append("\'  in \'");
          final Function1<Location,String> _function_7 = new Function1<Location,String>() {
            public String apply(final Location it) {
              Resource _eResource = it.eResource();
              URI _uRI = _eResource.getURI();
              return _uRI.toString();
            }
          };
          Iterable<String> _map_2 = IterableExtensions.<Location, String>map(externalLocationsWithConflictualID, _function_7);
          Set<String> _set = IterableExtensions.<String>toSet(_map_2);
          String _join_1 = IterableExtensions.join(_set, "\', \'");
          _builder.append(_join_1, "");
          _builder.append("\'.");
          _builder.newLineIfNotEmpty();
          final String msg = _builder.toString();
          final Function1<Location,URI> _function_8 = new Function1<Location,URI>() {
            public URI apply(final Location it) {
              Resource _eResource = it.eResource();
              return _eResource.getURI();
            }
          };
          Iterable<URI> _map_3 = IterableExtensions.<Location, URI>map(externalLocationsWithConflictualID, _function_8);
          final Set<URI> resourcesURIWithLocationConflictualID = IterableExtensions.<URI>toSet(_map_3);
          for (final URI resourceURIWithLocationConflictualID : resourcesURIWithLocationConflictualID) {
            {
              EList<IncludeDeclaration> _includes = targetPlatform.getIncludes();
              final Function1<IncludeDeclaration,Boolean> _function_9 = new Function1<IncludeDeclaration,Boolean>() {
                public Boolean apply(final IncludeDeclaration it) {
                  String _resolve = TargetPlatformValidator.this.resolver.resolve(it);
                  URI _createURI = URI.createURI(_resolve);
                  URI _uRI = resource.getURI();
                  URI _resolve_1 = _createURI.resolve(_uRI);
                  return Boolean.valueOf(resourceURIWithLocationConflictualID.equals(_resolve_1));
                }
              };
              final Iterable<IncludeDeclaration> includesError = IterableExtensions.<IncludeDeclaration>filter(_includes, _function_9);
              final Procedure1<IncludeDeclaration> _function_10 = new Procedure1<IncludeDeclaration>() {
                public void apply(final IncludeDeclaration it) {
                  TargetPlatformValidator.this.error(msg, it, TargetplatformPackage.Literals.INCLUDE_DECLARATION__IMPORT_URI);
                }
              };
              IterableExtensions.<IncludeDeclaration>forEach(includesError, _function_10);
            }
          }
        }
        int _size_1 = externalIDs.size();
        boolean _equals = (_size_1 == 1);
        if (_equals) {
          final Sets.SetView<String> diff = Sets.<String>symmetricDifference(externalIDs, internalIDs);
          boolean _isEmpty = diff.isEmpty();
          boolean _not = (!_isEmpty);
          if (_not) {
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append("The ID for location \'");
            _builder_1.append(locURI, "");
            _builder_1.append("\' must be unique across included target platforms and the current one. Found \'");
            String _head = IterableExtensions.<String>head(externalIDs);
            _builder_1.append(_head, "");
            _builder_1.append("\'  in \'");
            final Function1<Location,String> _function_9 = new Function1<Location,String>() {
              public String apply(final Location it) {
                Resource _eResource = it.eResource();
                URI _uRI = _eResource.getURI();
                return _uRI.toString();
              }
            };
            Iterable<String> _map_4 = IterableExtensions.<Location, String>map(externalLocations, _function_9);
            Set<String> _set_1 = IterableExtensions.<String>toSet(_map_4);
            String _join_2 = IterableExtensions.join(_set_1, "\', \'");
            _builder_1.append(_join_2, "");
            _builder_1.append("\'.");
            _builder_1.newLineIfNotEmpty();
            final String msg_1 = _builder_1.toString();
            final Function1<Location,Boolean> _function_10 = new Function1<Location,Boolean>() {
              public Boolean apply(final Location it) {
                boolean _and = false;
                String _iD = it.getID();
                boolean _notEquals = (!Objects.equal(_iD, null));
                if (!_notEquals) {
                  _and = false;
                } else {
                  String _iD_1 = it.getID();
                  boolean _contains = externalIDs.contains(_iD_1);
                  boolean _not = (!_contains);
                  _and = _not;
                }
                return Boolean.valueOf(_and);
              }
            };
            Iterable<Location> _filter_2 = IterableExtensions.<Location>filter(internalLocations, _function_10);
            final Procedure1<Location> _function_11 = new Procedure1<Location>() {
              public void apply(final Location it) {
                String _head = IterableExtensions.<String>head(externalIDs);
                Location _head_1 = IterableExtensions.<Location>head(externalLocations);
                String _uri = _head_1.getUri();
                TargetPlatformValidator.this.error(msg_1, it, TargetplatformPackage.Literals.LOCATION__ID, TargetPlatformValidator.CHECK__INCLUDED_LOCATION_CONFLICTUAL_ID, _head, _uri);
              }
            };
            IterableExtensions.<Location>forEach(_filter_2, _function_11);
          }
        }
        boolean _and = false;
        int _size_2 = externalIDs.size();
        boolean _lessThan = (_size_2 < 1);
        if (!_lessThan) {
          _and = false;
        } else {
          int _size_3 = internalIDs.size();
          boolean _greaterThan_1 = (_size_3 > 1);
          _and = _greaterThan_1;
        }
        if (_and) {
          StringConcatenation _builder_2 = new StringConcatenation();
          _builder_2.append("The ID for location \'");
          _builder_2.append(locURI, "");
          _builder_2.append("\' must be unique. Found \'");
          String _join_3 = IterableExtensions.join(internalIDs, "\', \'");
          _builder_2.append(_join_3, "");
          _builder_2.append("\'.");
          final String msg_2 = _builder_2.toString();
          final Procedure1<Location> _function_12 = new Procedure1<Location>() {
            public void apply(final Location it) {
              TargetPlatformValidator.this.error(msg_2, it, TargetplatformPackage.Literals.LOCATION__ID, TargetPlatformValidator.CHECK__LOCATION_CONFLICTUAL_ID);
            }
          };
          IterableExtensions.<Location>forEach(internalLocations, _function_12);
        }
      }
    }
  }
}
