/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.iterables;

import javafx.util.Pair;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.error.ElementsShouldMatch.elementsShouldMatch;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.presentation.PredicateDescription.GIVEN;
import static org.assertj.core.test.TestData.someInfo;

public class Iterables_assertMatches_Test extends IterablesBaseTest {

  @Test
  public void should_pass_if_element_satisfy_predicate_one_by_one() {
    iterables.assertMatches(someInfo(),
                            asList(10, 5, 2),
                            asList("20", "10", "4"),
                            (e1, e2) -> e1 * 2 == Integer.valueOf(e2), GIVEN);
  }

  @Test
  public void should_fail_if_there_are_not_matching_elements() {
    List<Integer> actual = asList(10, 5, 3, 7, 2);
    List<String> other = asList("20", "10", "4", "10", "4");
    thrown.expectAssertionError(elementsShouldMatch(actual, other, asList(new Pair<>(3, "4"), new Pair<>(7, "10")),
                                                    GIVEN));
    iterables.assertMatches(someInfo(), actual, other, (e1, e2) -> e1 * 2 == Integer.valueOf(e2), GIVEN);
  }

  @Test
  public void should_throw_error_if_actual_is_null() {
    thrown.expectAssertionError("%nExpecting actual not to be null");
    iterables.assertMatches(someInfo(), null, asList("1", "2"), String::equals, GIVEN);
  }

  @Test
  public void should_throw_error_if_predicate_is_null() {
    thrown.expectNullPointerException("The predicate to evaluate should not be null");
    iterables.assertMatches(someInfo(), actual, asList(1, 2), null, GIVEN);
  }

  @Test
  public void should_throw_error_if_target_is_null() {
    thrown.expectNullPointerException("The iterable of values to look for should not be null");
    iterables.assertMatches(someInfo(), actual, null, String::equals, GIVEN);
  }

  @Test
  public void should_fail_if_actual_size_is_not_equal_to_other_size() {
    List<String> actual = asList("1", "2");
    List<String> other = asList("1", "2", "3");
    thrown.expectAssertionError(shouldHaveSameSizeAs(actual, actual.size(),
                                                     other.size()).create(null, info.representation()));

    iterables.assertMatches(someInfo(), actual, other, String::equals, GIVEN);
  }
}
